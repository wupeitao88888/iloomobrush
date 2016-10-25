package com.iloomo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.iloomo.global.MApplication;

import java.io.File;

public class DbHelperBase extends
        android.database.sqlite.SQLiteOpenHelper {
    public static final int VERSION = 4;
    public static final String path = MApplication.getSDPath() +MApplication.DBMANE;

    public DbHelperBase(Context context) {
        super(context, path, null, VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE info(name VARCHAR(50),filename VARCHAR(50),path VARCHAR(1024) primary key, thid VARCHAR(54), done VARCHAR(54))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists info");
        onCreate(db);
    }




}
