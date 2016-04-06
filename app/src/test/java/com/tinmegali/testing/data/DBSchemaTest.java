package com.tinmegali.testing.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tinmegali.testing.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.hasItemInArray;


/**
 * ---------------------------------------------------
 * Created by Tin Megali on 03/04/16.
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
public class DBSchemaTest {

    private Context context;
    private DBSchema helper;
    private SQLiteDatabase db;

    @Before
    public void setup(){
        context = RuntimeEnvironment.application;
        helper = new DBSchema(context);
        db = helper.getReadableDatabase();
    }

    @After
    public void cleanup(){
        db.close();
    }

    @Test
    public void testDBCreated(){
        DBSchema helper = new DBSchema(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        // Verify is the DB is opening correctly
        assertTrue("DB didn't open", db.isOpen());
        db.close();
    }

    @Test
    public void testDBDelete(){
        assertTrue(context.deleteDatabase(DBSchema.DB_NAME));
    }

    @Test
    public void testDBCols() {
        Cursor c = db.query(DBSchema.TABLE_NOTES, null, null, null, null, null, null);
        assertNotNull( c );

        String[] cols = c.getColumnNames();
        assertThat("Column not implemented: " + DBSchema.TB_NOTES.DATE,
                cols, hasItemInArray(DBSchema.TB_NOTES.DATE));
        assertThat("Column not implemented: " + DBSchema.TB_NOTES.ID,
                cols, hasItemInArray(DBSchema.TB_NOTES.ID));
        assertThat("Column not implemented: " + DBSchema.TB_NOTES.NOTE,
                cols, hasItemInArray(DBSchema.TB_NOTES.NOTE));

        c.close();
    }
}