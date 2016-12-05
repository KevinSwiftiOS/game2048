package com.example.hcnucai.game2048;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hcnucai on 2016/12/5.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    //创建数据库
    public static final String CREATE_CONTACT = "create table person ("
            + "username varchar(80) primary key, " + "password varchar(80), "
            + "highscore int)";
    private Context mcontext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
        sqLiteDatabase.execSQL(CREATE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
