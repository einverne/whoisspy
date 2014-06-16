package com.einverne.whoisspy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by EinVerne on 2014/6/13.
 */
public class WordsDataSource {

    private SQLiteDatabase database;
    private WordsLibOpenHelper dbHelper;

    public WordsDataSource(Context context) {
        dbHelper = new WordsLibOpenHelper(context);
    }

    public void open() throws SQLiteException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public boolean addWordsPair(String first,String second){
        ContentValues values = new ContentValues();
        values.put(WordsLibOpenHelper.COLUMN_ONE,first);
        values.put(WordsLibOpenHelper.COLUMN_TWO,second);
        values.put(WordsLibOpenHelper.COLUMN_THREE,false);
        long insertId = database.insert(WordsLibOpenHelper.TABLE_NAME,null,values);
        if (insertId != -1){
            return  true;
        }else {
            return false;
        }
    }

    public void clearDatabase(){
        database.execSQL("delete * from "+WordsLibOpenHelper.TABLE_NAME);
    }
}
