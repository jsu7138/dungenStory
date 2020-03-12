package com.example.dgstory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyHelper extends SQLiteOpenHelper {

    public final static String KEY_NUM = "_num";            //스테이지 값
    public final static String KEY_FLOOR = "_floor";        //
    public final static String KEY_TYPE = "_type";          //오픈 유무
    public final static String TABLE_NAME = "dungeonstage";

    public static String QUERY_SELECT_ALL = String.format("select count(*) from %s",TABLE_NAME);
    //이지
    public final static String QUERY_SELECT_EASY_OPEN = String.format("select %s from %s WHERE %s between '1' and '10' and %s LIKE 'open'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    public final static String QUERY_SELECT_EASY_CLOSE = String.format("select %s from %s WHERE %s between '1' and '10' and %s LIKE 'close'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    public final static String QUERY_SELECT_EASY_CLEAR = String.format("select %s from %s WHERE %s between '1' and '10' and %s LIKE 'clear'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    //노말
    public final static String QUERY_SELECT_NORMAL_OPEN = String.format("select %s from %s WHERE %s between '11' and '20' and %s LIKE 'open'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    public final static String QUERY_SELECT_NORMAL_CLOSE = String.format("select %s from %s WHERE %s between '11' and '20' and %s LIKE 'close'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    public final static String QUERY_SELECT_NORMAL_CLEAR = String.format("select %s from %s WHERE %s between '11' and '20' and %s LIKE 'clear'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    //하드
    public final static String QUERY_SELECT_HARD_OPEN = String.format("select %s from %s WHERE %s between '21' and '30' and %s LIKE 'open'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    public final static String QUERY_SELECT_HARD_CLOSE = String.format("select %s from %s WHERE %s between '21' and '30' and %s LIKE 'close'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    public final static String QUERY_SELECT_HARD_CLEAR = String.format("select %s from %s WHERE %s between '21' and '30' and %s LIKE 'clear'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    //베리하드
    public final static String QUERY_SELECT_VERYHARD_OPEN = String.format("select %s from %s WHERE %s between '31' and '40' and %s LIKE 'open'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    public final static String QUERY_SELECT_VERYHARD_CLOSE = String.format("select %s from %s WHERE %s between '31' and '40' and %s LIKE 'close'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);
    public final static String QUERY_SELECT_VERYHARD_CLEAR = String.format("select %s from %s WHERE %s between '31' and '40' and %s LIKE 'clear'",KEY_FLOOR,TABLE_NAME,KEY_NUM,KEY_TYPE);


    public MyHelper(Context context) {
        super(context,"MyData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String query = String.format("Create table %s ("+
                    "%s INTEGER PRIMARY KEY, %s INTEGER,%s TEXT);",TABLE_NAME,KEY_NUM,KEY_FLOOR,KEY_TYPE);
            db.execSQL(query);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void delete() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format("delete from %s",TABLE_NAME));
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
