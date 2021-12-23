package com.example.assignment3;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AddMemoDialogListener {

     FloatingActionButton fb;
   // com.example.assignment3.MainActivity.DBAdapter db = new com.example.assignment3.MainActivity.DBAdapter(this)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    void setUpViews(){
        fb= (FloatingActionButton)findViewById(R.id.floatingActionButton);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });
    }

    public void OpenDialog(){
        AddMemoDialog m = new AddMemoDialog();
        m.show(getSupportFragmentManager(),"Add Memo Dialog");
    }

    @Override
    public void applyText(String memo) {

    }

    public static class DBAdapter {
        static final String KEY_ROWID = "_id";
        static final String KEY_MEMO = "memo";
        static final String KEY_DATE = "date";
        static final String TAG = "com.example.assignment3.MainActivity.DBAdapter";
        static final String DATABASE_NAME = "MyDB";
        static final String DATABASE_TABLE = "notes";
        static final int DATABASE_VERSION = 1;
        static final String DATABASE_CREATE =
                "create table notes (_id integer primary key autoincrement, "
                        + "memo text not null,"+" date Date not null);";

        final Context context;
        DataBaseHelper DBHelper;
        SQLiteDatabase db;

        public DBAdapter(Context ctx) {
            this.context = ctx;
            DBHelper = new DataBaseHelper(context);
        }

        //---opens the database---
        public DBAdapter open() throws SQLException {
            db = DBHelper.getWritableDatabase();
            return this;
        }

        //---insert a contact into the database---
        public long insertMemo(String memo) {
            Date currentTime = Calendar.getInstance().getTime();
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_MEMO, memo);
            initialValues.put(KEY_DATE, String.valueOf(currentTime));
            return db.insert(DATABASE_TABLE, null, initialValues);
        }

        //---closes the database---
        public void close() {
            DBHelper.close();
        }

        //---retrieves all the contacts---
        public Cursor getMemos() {
            return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_MEMO,
                    KEY_DATE}, null, null, null, null, null);
        }

        //---retrieves a particular contact---
        public Cursor getMemos(long rowId) throws SQLException {
            Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_MEMO, KEY_DATE},
                    KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;

        }

        //---updates a contact---

        public boolean updateMemo(long rowId, String memo) {
            Date currentTime = Calendar.getInstance().getTime();
            ContentValues args = new ContentValues();
            args.put(KEY_MEMO, memo);
            args.put(KEY_DATE, String.valueOf(currentTime));
            return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
        }

        //---deletes a particular contact---
        public boolean deleteMemo(long rowId) {
            return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
        }

        private class DataBaseHelper extends SQLiteOpenHelper {

            DataBaseHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);

            }
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                try {
                    sqLiteDatabase.execSQL(DATABASE_CREATE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                Log.w(TAG, "Upgrading database from version " + i + " to "
                        + i1 + ", which will destroy all old data");
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
                onCreate(sqLiteDatabase);

            }
        }
    }
}
