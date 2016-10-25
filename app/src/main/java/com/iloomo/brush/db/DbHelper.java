package com.iloomo.brush.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.iloomo.db.DbHelperBase;

import java.io.File;
import java.io.IOException;

/**
 * Created by wupeitao on 16/3/17.
 */
public class DbHelper extends DbHelperBase {
    public DbHelper(Context context) {

        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

        db.execSQL("create table exec(exec_id varchar(50) primary key,device_id varchar(50),exec_date varchar(50),exec_time varchar(50),pnumber varchar(20),networktype varchar(10),type varchar(20))");
        db.execSQL("create table mpdata(mp_id varchar(50),exec_id varchar(50),m_products varchar(50),screen_width varchar(50),screen_hight varchar(50),m_apparatus varchar(20),rom_version varchar(20),mac_address varchar(20) primary key,type varchar(20),simoperatorname varchar(100),simoperator varchar(100))");
        db.execSQL("create table packagedata(task_id varchar(50) primary key,exec_id varchar(50),timeonpage varchar(50),package_name varchar(50),countlist varchar(50),type varchar(20),down_url varchar(20))");
        db.execSQL("create table vpndata(vpnid varchar(50) primary key,conip varchar(50),exec_id varchar(50),vpnname varchar(50),vpnpwd varchar(50))");
        db.execSQL("create table checktime(servertime varchar(50) primary key)");
        db.execSQL("create table phoneinfo(imei varchar(50),name varchar(30) primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        db.execSQL("drop table if exists exec");
        db.execSQL("drop table if exists mpdata");
        db.execSQL("drop table if exists packagedata");
        db.execSQL("drop table if exists vpndata");
        db.execSQL("drop table if exists checktime");
        db.execSQL("drop table if exists phoneinfo");
        onCreate(db);
    }
}
