package com.tinmegali.testing;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tinmegali.testing.data.DAO;
import com.tinmegali.testing.data.DBSchema;
import com.tinmegali.testing.models.Note;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalMatchers.aryEq;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 18/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
@RunWith(RobolectricGradleTestRunner.class)
// To use Robolectric you need to setup some constants.
// Change it according to your needs.
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class DAOTest {

    private DAO dao;
    private DAO daoWMocks;
    private SQLiteDatabase dbM;
    private Cursor cM;
    private Note noteStub;

    @Before
    public void setup() {
        Context context = RuntimeEnvironment.application;
        dao = new DAO(context);

        setupNote();
        daoWMocks = new DAO(context, setupHelperMock() );
    }

    /**
     * Setup a DBSchema mock
     * @return  Returns the mocked obj
     */
    private DBSchema setupHelperMock(){
        // create the mocks
        DBSchema helperM = Mockito.mock(DBSchema.class);
        dbM = Mockito.mock(SQLiteDatabase.class);

        // Define method's results for the mock obj
        when(helperM.getReadableDatabase()).thenReturn(dbM);
        when(helperM.getWritableDatabase()).thenReturn(dbM);
        return helperM;
    }

    /**
     * Setup a Note to be used in the tests
     */
    private void setupNote() {
        noteStub = new Note();
        noteStub.setText("MockNote");
        noteStub.setDate("00/00/00");
        noteStub.setId(111);
    }

    /**
     * Setup a mock Cursor
     * that returns a given Note data
     */
    private void setupMockCursor(Note noteToRet){
        // create the mock cursor
        cM = Mockito.mock(Cursor.class);
        // define method's return
        when(cM.moveToFirst()).thenReturn(true);
        int idId = 0;
        int idNote = 1;
        int idDate = 2;
        // define method's return
        when(cM.getColumnIndexOrThrow(DBSchema.TB_NOTES.ID)).thenReturn(idId);
        when(cM.getColumnIndexOrThrow(DBSchema.TB_NOTES.NOTE)).thenReturn(idNote);
        when(cM.getColumnIndexOrThrow(DBSchema.TB_NOTES.DATE)).thenReturn(idDate);

        // define method's return
        when(cM.getInt(idId)).thenReturn(noteToRet.getId());
        when(cM.getString(idNote)).thenReturn(noteToRet.getText());
        when(cM.getString(idDate)).thenReturn(noteToRet.getDate());
    }

    /**
     * Setup a mockDB query search.
     * @param noteMockId    Note id to search
     * @param cursor        Query result
     */
    private void setupQueryMock(int noteMockId, Cursor cursor) {
        String[] selArgs = new String[] { Integer.toString(noteMockId)};
        // Defining DB query return
        when(dbM.query(
                anyString(),
                (String[]) isNull(),
                anyString(),
                aryEq(selArgs),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(cursor);
    }

    /**
     * Testing DAO with a db mock
     */
    @Test
    public void getNoteTestMock(){

        setupMockCursor(noteStub);
        setupQueryMock(noteStub.getId(), cM);

        Note note = daoWMocks.getNote(noteStub.getId());
        // Verify if specified method was called
        verify(cM).moveToFirst();
        assertNotNull(note);
        assertEquals(note.getId(), noteStub.getId());
        assertEquals(note.getText(), noteStub.getText());
        assertEquals(note.getDate(), noteStub.getDate());
        // Verify if specified method was called
        verify(cM).close();
        verify(dbM).close();
    }

    /**
     * Testing DAO with a db mock
     */
    @Test
    public void getNoteTestMockFail(){
        setupQueryMock(noteStub.getId(), null);
        Note note = daoWMocks.getNote(noteStub.getId());
        // result should be null
        assertNull(note);
        // should close db
        verify(dbM).close();
    }

    /**
     * Testing DAO with a REAL db
     */
    @Test
    public void insertNoteTest() {
        Note noteInserted = dao.insertNote(noteStub);
        assertNotNull(noteInserted);
        assertEquals(noteStub.getText(), noteInserted.getText());
    }

    /**
     * Testing DAO with a REAL db
     */
    @Test
    public void getNoteTest() {
        Note noteInserted = dao.insertNote(noteStub);

        Note note = dao.getNote(noteInserted.getId());
        assertNotNull(note);
        assertEquals(note.getText(), noteStub.getText());
    }

    /**
     * Testing DAO with a REAL db
     */
    @Test
    public void noteListTest() {
        ArrayList<String> noteTexts = new ArrayList<>();
        noteTexts.add( "note1" );
        noteTexts.add( "note2" );
        noteTexts.add( "note3" );

        for( int i=0; i<noteTexts.size(); i++){
            Note note = new Note(noteTexts.get(i), "00/00/00");
            dao.insertNote(note);
        }

        ArrayList<Note> notes = dao.getAllNotes();
        assertNotNull( notes );
        assertEquals(notes.size(), noteTexts.size());
    }

    /**
     * Testing DAO with a REAL db
     */
    @Test
    public void deleteNoteTest() {
        Note noteInserted = dao.insertNote(noteStub);

        long delResult = dao.deleteNote( noteInserted );
        assertEquals(1, delResult);
    }
}
