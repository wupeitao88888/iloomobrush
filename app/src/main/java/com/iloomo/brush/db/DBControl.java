package com.iloomo.brush.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.iloomo.brush.bean.ExeTime;
import com.iloomo.brush.bean.MpData;
import com.iloomo.brush.bean.PackageData;
import com.iloomo.brush.bean.VpnData;
import com.iloomo.db.DBbase;
import com.iloomo.db.DatabaseManager;
import com.iloomo.db.DbHelperBase;
import com.iloomo.utils.L;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wupeitao on 16/3/17.
 */
public class DBControl extends DBbase {

    public static String DB_VERSION = "28";

    public DBControl(Context context, DbHelperBase DbHelperBase) {
        super(context, DbHelperBase);
    }

    public static DBControl dbControl;

    public static DBControl getInstance(Context context) {
        if (dbControl == null) {
            DbHelper dbHelper = new DbHelper(context);
            dbControl = new DBControl(context, dbHelper);
        }
        return dbControl;
    }

    public synchronized void insertExec(ExeTime exeTime) {
        try {
            SQLiteDatabase writableDatabase = DatabaseManager.getInstance()
                    .openDatabase();
            writableDatabase
                    .execSQL(
                            "insert into exec(exec_id,device_id,exec_date,exec_time,pnumber,networktype,type" +
                                    ")values(?,?,?,?,?,?,?)",
                            new Object[]{exeTime.getExec_id(), exeTime.getDevice_id(), exeTime.getExec_date(),
                                    exeTime.getExec_time(), exeTime.getPnumber(), exeTime.getNetworktype(),
                                    "0"});
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            L.e("exec插入数据失败（原因：已存在）！");
        }
    }


    public synchronized void updateExec(String type, String exec_id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("type", type);
        db.update("exec", values, "exec_id=?",
                new String[]{exec_id});
        DatabaseManager.getInstance().closeDatabase();
    }

    public synchronized boolean isTodayTask(String time) {
        SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
                .readDatabase();
        Cursor cursor = readableDatabase.rawQuery(
                "select * from exec where type=? and exec_date=?",
                new String[]{"0", time});

        String device_id = "";
        while (cursor.moveToNext()) {
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
        }

        Cursor cursorPriority = readableDatabase.rawQuery(
                "select * from exec where type=? and exec_date=?",
                new String[]{"1", time});
        String device_idPriority = "";
        while (cursorPriority.moveToNext()) {
            device_idPriority = cursorPriority.getString(cursorPriority.getColumnIndex("device_id"));
        }

        cursorPriority.close();
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        if (!TextUtils.isEmpty(device_idPriority)) {
            return false;
        }
        if (TextUtils.isEmpty(device_id)) {
            return true;
        } else {
            return false;
        }

    }

    public synchronized boolean isOverTask(String time) {
        SQLiteDatabase readableDatabase = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = readableDatabase.rawQuery(
                "select * from exec where type=? and exec_date=?",
                new String[]{"0", time});

        String device_id = "";
        while (cursor.moveToNext()) {
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
        }

        Cursor cursorPriority = readableDatabase.rawQuery(
                "select * from exec where type=? and exec_date=?",
                new String[]{"1", time});
        String device_idPriority = "";
        while (cursorPriority.moveToNext()) {
            device_idPriority = cursorPriority.getString(cursorPriority.getColumnIndex("device_id"));
        }

        cursorPriority.close();
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        if (!TextUtils.isEmpty(device_idPriority)) {
            return false;
        }
        if (TextUtils.isEmpty(device_id)) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized int isTaskSelect(String time) {
        SQLiteDatabase readableDatabase = DatabaseManager.getInstance().openDatabase();
        int i = 0;
        Cursor cursor = readableDatabase.rawQuery(
                "select * from exec where type=? and exec_date=?",
                new String[]{"0", time});

        String device_id = "";
        while (cursor.moveToNext()) {
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
            i++;
        }

        Cursor cursorPriority = readableDatabase.rawQuery(
                "select * from exec where type=? and exec_date=?",
                new String[]{"1", time});
        String device_idPriority = "";
        while (cursorPriority.moveToNext()) {
            device_idPriority = cursorPriority.getString(cursorPriority.getColumnIndex("device_id"));
            i++;
        }

        cursorPriority.close();
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return i;
    }


    public synchronized List<ExeTime> selectExec(String types, String exec_dates) {
        SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
                .readDatabase();
        Cursor cursor = readableDatabase.rawQuery(
                "select * from exec where type=? and exec_date=?",
                new String[]{types, exec_dates});

        List<ExeTime> exeTimes = new ArrayList<>();
        while (cursor.moveToNext()) {
            String device_id = "";
            String exec_date = "";
            String exec_time = "";
            String pnumber = "";
            String networktype = "";
            String type = "";
            device_id = cursor.getString(cursor.getColumnIndex("device_id"));
            exec_date = cursor.getString(cursor.getColumnIndex("exec_date"));
            exec_time = cursor.getString(cursor.getColumnIndex("exec_time"));
            pnumber = cursor.getString(cursor.getColumnIndex("pnumber"));
            networktype = cursor.getString(cursor.getColumnIndex("networktype"));
            type = cursor.getString(cursor.getColumnIndex("type"));
            String exec_id = cursor.getString(cursor.getColumnIndex("exec_id"));
            ExeTime exeTime = new ExeTime();
            exeTime.setExec_id(exec_id);
            exeTime.setDevice_id(device_id);
            exeTime.setExec_date(exec_date);
            exeTime.setExec_time(exec_time);
            exeTime.setPnumber(pnumber);
            exeTime.setNetworktype(networktype);
            exeTime.setType(type);
            exeTimes.add(exeTime);
        }

        DatabaseManager.getInstance().closeDatabase();
        cursor.close();
        return exeTimes;
    }


    public synchronized void insertMpData(MpData mpData, String exec_id) {
        try {
            SQLiteDatabase writableDatabase = DatabaseManager.getInstance()
                    .openDatabase();
            writableDatabase
                    .execSQL(
                            "insert into mpdata(mp_id,exec_id,m_products,screen_width,screen_hight,m_apparatus,rom_version" +
                                    ",mac_address,type,simoperatorname,simoperator)values(?,?,?,?,?,?,?,?,?,?,?)",
                            new Object[]{mpData.getMp_id() + "_" + exec_id, exec_id, mpData.getM_products(), mpData.getScreen_width(), mpData.getScreen_hight(), mpData.getM_apparatus(), mpData.getRom_version(), mpData.getMac_address(), "0",mpData.getSimoperatorname(),mpData.getSimoperator()});
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            L.e("mpdata插入数据失败（原因：已存在）！");
        }
    }


    public synchronized List<MpData> selectMpData(String exec_id) {
        SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
                .readDatabase();
        Cursor cursor = readableDatabase.rawQuery(
                "select * from mpdata where exec_id=?",
                new String[]{exec_id});

        List<MpData> mpDatas = new ArrayList<>();
        while (cursor.moveToNext()) {
            String mp_id = cursor.getString(cursor.getColumnIndex("mp_id"));
            String m_products = cursor.getString(cursor.getColumnIndex("m_products"));
            String screen_width = cursor.getString(cursor.getColumnIndex("screen_width"));
            String screen_hight = cursor.getString(cursor.getColumnIndex("screen_hight"));
            String m_apparatus = cursor.getString(cursor.getColumnIndex("m_apparatus"));
            String rom_version = cursor.getString(cursor.getColumnIndex("rom_version"));
            String mac_address = cursor.getString(cursor.getColumnIndex("mac_address"));
            String simoperatorname = cursor.getString(cursor.getColumnIndex("simoperatorname"));
            String simoperator = cursor.getString(cursor.getColumnIndex("simoperator"));
            MpData mpData = new MpData();
            mpData.setMp_id(mp_id);
            mpData.setM_products(m_products);
            mpData.setScreen_width(screen_width);
            mpData.setScreen_hight(screen_hight);
            mpData.setM_apparatus(m_apparatus);
            mpData.setRom_version(rom_version);
            mpData.setMac_address(mac_address);
            mpData.setSimoperator(simoperator);
            mpData.setSimoperatorname(simoperatorname);
            mpDatas.add(mpData);
        }

        DatabaseManager.getInstance().closeDatabase();
        cursor.close();
        return mpDatas;
    }


    public synchronized void insertPackagedata(PackageData packageData, String exec_id) {
        try {
            SQLiteDatabase writableDatabase = DatabaseManager.getInstance()
                    .openDatabase();
            writableDatabase
                    .execSQL(
                            "insert into packageData(task_id,exec_id,timeonpage,package_name,countlist,type,down_url)values(?,?,?,?,?,?,?)",
                            new Object[]{packageData.getTask_id() + "_" + exec_id, exec_id, packageData.getTimeonpage(), packageData.getPackage_name(), packageData.getCountlist(), "0", packageData.getDown_url()});
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            L.e("packageData插入数据失败（原因：已存在）！");
        }
    }

    public synchronized List<PackageData> selectPackagedata(String types, String exec_id) {
        SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
                .readDatabase();
        Cursor cursor = readableDatabase.rawQuery(
                "select * from packageData where exec_id=? and type=?",
                new String[]{exec_id, types});

        List<PackageData> packageDatas = new ArrayList<>();
        while (cursor.moveToNext()) {
            String task_id = cursor.getString(cursor.getColumnIndex("task_id"));
            String timeonpage = cursor.getString(cursor.getColumnIndex("timeonpage"));
            String package_name = cursor.getString(cursor.getColumnIndex("package_name"));
            String countlist = cursor.getString(cursor.getColumnIndex("countlist"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            PackageData packageData = new PackageData();
            packageData.setTask_id(task_id);
            packageData.setType(type);
            packageData.setCountlist(countlist);
            packageData.setPackage_name(package_name);
            packageData.setTimeonpage(timeonpage);
            packageData.setDown_url(cursor.getString(cursor.getColumnIndex("down_url")));
            packageDatas.add(packageData);
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return packageDatas;
    }

    public synchronized int selectPackagedataAll() {
        SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
                .readDatabase();
        Cursor cursor = readableDatabase.rawQuery(
                "select * from packageData where type=?",
                new String[]{"0"});

        List<PackageData> packageDatas = new ArrayList<>();
        while (cursor.moveToNext()) {
            String task_id = cursor.getString(cursor.getColumnIndex("task_id"));
            String timeonpage = cursor.getString(cursor.getColumnIndex("timeonpage"));
            String package_name = cursor.getString(cursor.getColumnIndex("package_name"));
            String countlist = cursor.getString(cursor.getColumnIndex("countlist"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            PackageData packageData = new PackageData();
            packageData.setTask_id(task_id);
            packageData.setType(type);
            packageData.setCountlist(countlist);
            packageData.setPackage_name(package_name);
            packageData.setTimeonpage(timeonpage);
            packageData.setDown_url(cursor.getString(cursor.getColumnIndex("down_url")));
            packageDatas.add(packageData);
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return packageDatas.size();
    }


    public synchronized void updatePackagedata(String exec_id, String task_id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("type", "1");
        db.update("packageData", values, "exec_id=? and task_id=?", new String[]{exec_id, task_id});
        DatabaseManager.getInstance().closeDatabase();
    }

    public synchronized void insertVpndata(VpnData vpnData, String exec_id) {
        try {
            SQLiteDatabase writableDatabase = DatabaseManager.getInstance()
                    .openDatabase();
            writableDatabase
                    .execSQL(
                            "insert into vpndata(vpnid,conip,exec_id,vpnname,vpnpwd)values(?,?,?,?,?)",
                            new Object[]{vpnData.getConip() + "_" + exec_id, vpnData.getConip(), exec_id, vpnData.getVpnname(), vpnData.getVpnpwd()});
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            L.e("vpndata插入数据失败（原因：已存在）！");
        }
    }

    public synchronized List<VpnData> selectVpnData(String exec_id) {
        SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
                .readDatabase();
        Cursor cursor = readableDatabase.rawQuery(
                "select * from vpndata where exec_id=?",
                new String[]{exec_id});

        List<VpnData> vpnDatas = new ArrayList<>();
        while (cursor.moveToNext()) {
            String conip = cursor.getString(cursor.getColumnIndex("conip"));
            String vpnname = cursor.getString(cursor.getColumnIndex("vpnname"));
            String vpnpwd = cursor.getString(cursor.getColumnIndex("vpnpwd"));

            VpnData vpnData = new VpnData();
            vpnData.setConip(conip);
            vpnData.setVpnname(vpnname);
            vpnData.setVpnpwd(vpnpwd);
            vpnDatas.add(vpnData);
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return vpnDatas;
    }


    public synchronized void insertServar(String servertime) {
        try {
            SQLiteDatabase writableDatabase = DatabaseManager.getInstance()
                    .openDatabase();
            writableDatabase
                    .execSQL(
                            "insert into checktime(servertime)values(?)",
                            new Object[]{servertime});
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            L.e("checktime插入数据失败（原因：已存在）！" + e);
        }
    }

    public synchronized String selectServar() {
        SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
                .readDatabase();
        Cursor cursor = readableDatabase.rawQuery(
                "select * from checktime",
                new String[]{});
        String servertime = "";
        while (cursor.moveToNext()) {
            servertime = cursor.getString(cursor.getColumnIndex("servertime"));
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return servertime;
    }


    public synchronized void deleteAllTab() {
        SQLiteDatabase db = DatabaseManager.getInstance()
                .openDatabase();
        db.execSQL("drop table if exists exec");
        db.execSQL("drop table if exists mpdata");
        db.execSQL("drop table if exists packagedata");
        db.execSQL("drop table if exists vpndata");
        db.execSQL("drop table if exists checktime");
        db.execSQL("drop table if exists info");
//        db.execSQL("drop table if exists phoneinfo");
        DatabaseManager.getInstance().closeDatabase();
        createAllTab();
    }

    //    创建表
    public synchronized void createAllTab() {
        SQLiteDatabase db = DatabaseManager.getInstance()
                .openDatabase();
        db.execSQL("create table exec(exec_id varchar(50) primary key,device_id varchar(50),exec_date varchar(50),exec_time varchar(50),pnumber varchar(20),networktype varchar(10),type varchar(20))");
        db.execSQL("create table mpdata(mp_id varchar(50),exec_id varchar(50),m_products varchar(50),screen_width varchar(50),screen_hight varchar(50),m_apparatus varchar(20),rom_version varchar(20),mac_address varchar(20) primary key,type varchar(20),simoperatorname varchar(100),simoperator varchar(100))");
        db.execSQL("create table packagedata(task_id varchar(50) primary key,exec_id varchar(50),timeonpage varchar(50),package_name varchar(50),countlist varchar(50),type varchar(20),down_url varchar(20))");
        db.execSQL("create table vpndata(vpnid varchar(50) primary key,conip varchar(50),exec_id varchar(50),vpnname varchar(50),vpnpwd varchar(50))");
        db.execSQL("create table checktime(servertime varchar(50) primary key)");
        db.execSQL("CREATE TABLE info(name VARCHAR(50),filename VARCHAR(50),path VARCHAR(1024), thid VARCHAR(54), done VARCHAR(54), PRIMARY KEY(path, thid))");
        db.execSQL("create table phoneinfo(imei varchar(50),name varchar(30) primary key)");
        DatabaseManager.getInstance().closeDatabase();
    }


    /****
     * 插入手机的IMEI  ******************start***********************
     */
    public synchronized void insertPhoneTab(String imei) {
        try {

            SQLiteDatabase writableDatabase = DatabaseManager.getInstance()
                    .openDatabase();
            writableDatabase
                    .execSQL(
                            "insert into phoneinfo(imei,name)values(?,?)",
                            new Object[]{imei, "imei"});
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            L.e("imei插入数据失败（原因：已存在）！");
        }
    }

    public synchronized String selectPhoneTab() {
        SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
                .readDatabase();
        Cursor cursor = readableDatabase.rawQuery(
                "select imei from phoneinfo",
                new String[]{});

        String mp_id = "";
        while (cursor.moveToNext()) {
            mp_id = cursor.getString(cursor.getColumnIndex("imei"));
        }

        DatabaseManager.getInstance().closeDatabase();
        cursor.close();
        return mp_id;
    }
    /*******
     * **************************end***********************************
     */

}
