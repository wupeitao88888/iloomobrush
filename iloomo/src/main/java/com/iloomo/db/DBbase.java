package com.iloomo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iloomo.bean.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wupeitao on 16/3/17.
 */
public class DBbase {
//    private DBOpenHelper helper;

    public DBbase(Context context, DbHelperBase DbHelperBase) {
        DatabaseManager.initializeInstance(DbHelperBase);
    }

    public synchronized void insert(Info info) {
        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.execSQL("INSERT INTO info(path, thid, done) VALUES(?, ?, ?)", new Object[] { info.getPath(), info.getThid(), info.getDone() });
            DatabaseManager.getInstance().closeDatabase();
        }catch (Exception e){

        }
    }

    public synchronized void delete(String path, int thid) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM info WHERE path=? AND thid=?", new Object[] { path, thid });
        DatabaseManager.getInstance().closeDatabase();
    }

    public synchronized void update(Info info) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("UPDATE info SET done=? WHERE path=? AND thid=?", new Object[] { info.getDone(), info.getPath(), info.getThid() });
        DatabaseManager.getInstance().closeDatabase();
    }

    public synchronized Info query(String path, int thid) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = db.rawQuery("SELECT path, thid, done FROM info WHERE path=? AND thid=?", new String[] { path, String.valueOf(thid) });
        Info info = null;
        if (c.moveToNext())
            info = new Info();
        info.setPath(c.getString(c.getColumnIndex("path")));
        info.setThid(c.getString(c.getColumnIndex("thid")));
        info.setDone(c.getString(c.getColumnIndex("done")));
        c.close();
        DatabaseManager.getInstance().closeDatabase();
        return info;
    }

    public synchronized void deleteAll(String path, int len) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = db.rawQuery("SELECT SUM(CAST(done AS signed)) FROM info WHERE path=?", new String[] { path });
        if (c.moveToNext()) {
            int result = c.getInt(0);
            if (result == len)
                db.execSQL("DELETE FROM info WHERE path=? ", new Object[] { path });
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public synchronized List<String> queryUndone() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT path FROM info", null);
        List<String> pathList = new ArrayList<String>();
        while (c.moveToNext())
            pathList.add(c.getString(0));
        c.close();
        DatabaseManager.getInstance().closeDatabase();
        return pathList;
    }

}
