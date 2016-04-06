package com.tinmegali.testing;

import android.content.Context;

import com.tinmegali.testing.data.DAO;
import com.tinmegali.testing.main.activity.model.MainModel;
import com.tinmegali.testing.main.activity.presenter.MainPresenter;
import com.tinmegali.testing.models.Note;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class MainModelTest {

    private MainModel model;
    private DAO DAOMock, DAO;
    private MainPresenter presenterM;

    @Before
    public void setup() {
        DAOMock = mock(DAO.class);
        DAO = new DAO(RuntimeEnvironment.application);

        setupSingleNote();
        setupNotesM(3);
        setupDAOMock();

        presenterM = mock(MainPresenter.class);
        model = new MainModel(presenterM, DAOMock);
        model.mNotes = new ArrayList<>();
    }

    @After
    public void after(){
        reset(presenterM);
    }


    private Note createNote(String text) {
        Note note = new Note();
        note.setText(text);
        note.setDate("some date");
        return note;
    }

    private Note singleNote;
    private void setupSingleNote(){
        singleNote = new Note();
        singleNote.setText("SingleNote");
        singleNote.setId(111);
        singleNote.setDate("00/00/00");
    }

    private ArrayList<Note> notesM;

    private void setupNotesM(int qtd){
        notesM = new ArrayList<>();
        for( int i=0; i<qtd; i++) {
            Note note = new Note();
            note.setId(i);
            note.setText("Note[" + i + "]");
            note.setDate(i + "/" + i + "/" + i);
            notesM.add(note);
        }
    }

    private void setupDAOMock(){
        DAOMock = mock(DAO.class);
        // retuning notes list
        when(DAOMock.getAllNotes())
                .thenReturn(notesM);

        when(DAOMock.getNote(anyInt()))
                .thenReturn(singleNote);
        when(DAOMock.insertNote(notesM.get(2)))
                .thenReturn(notesM.get(2));
        when(DAOMock.insertNote(singleNote))
                .thenReturn(singleNote);
    }

    @Test
    public void loadDataTest(){
        model.loadData();
        assertEquals(model.mNotes.size(), notesM.size());
    }

    @Test
    public void getNotePosTest(){
        model.loadData();
        assertEquals(model.getNotePosition(notesM.get(1)), 1);
        assertEquals(model.getNotePosition(singleNote ), -1);
    }

    @Test
    public void insertNoteTest() {
        int pos = model.insertNote(notesM.get(2));
        assertTrue(pos == 2);

        int posErr = model.insertNote(singleNote);
        verify(DAOMock, VerificationModeFactory.atLeast(2)).getAllNotes();
        assertTrue(posErr == -1);
    }

    /**
     * Testing with a real DAO class
     */
    @Test
    public void deleteNoteTest() {
        Note note = createNote("testNote");
        Note insertedNote = DAO.insertNote(note);
        Context context = RuntimeEnvironment.application;
        DAO DAO = new DAO( context );
        model = new MainModel(mock(MainPresenter.class), DAO);
        model.mNotes = new ArrayList<>();
        model.mNotes.add(insertedNote);

        assertTrue(model.deleteNote(insertedNote, 0));

        Note fakeNote = createNote("fakeNote");
        assertFalse(model.deleteNote(fakeNote, 0));
    }
}
