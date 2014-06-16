package com.einverne.whoisspy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by EinVerne on 2014/6/13.
 */
public class WordsLibOpenHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wordslib.db";
    public static final String TABLE_NAME = "wordslib";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ONE = "WORD_ONE";
    public static final String COLUMN_TWO = "WORD_TWO";
    public static final String COLUMN_THREE = "isUsed";
    private static final String WORDSLIB_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " integer primary key autoincrement, "+
                    COLUMN_ONE + " TEXT not null, " +
                    COLUMN_TWO + " TEXT not null, " +
                    COLUMN_THREE+" INTEGER );";

    WordsLibOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WORDSLIB_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
