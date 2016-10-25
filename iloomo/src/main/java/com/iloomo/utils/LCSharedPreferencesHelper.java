package com.iloomo.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * <pre>
 * wpt
 * </pre>
 */
public class LCSharedPreferencesHelper {

    public static String TAG = "SharedPreferencesHelper";
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Context mContext;
    public final static String ILOOMO = "iloomo";
    public final static String UPDATE_DB = "updat_db";
    public final static String START = "TASKSTART";
    public final static String END = "TASKEND";
    private static LCSharedPreferencesHelper sh;

    public static synchronized LCSharedPreferencesHelper instance(
            Context context, String pName) {
        if (sh == null) {
            sh = new LCSharedPreferencesHelper(context, pName);
        }
        return sh;
    }

    public LCSharedPreferencesHelper(Context context, String pName) {
        this.mContext = context;
        sp = mContext.getSharedPreferences(pName, 0);
        editor = sp.edit();
    }

    public void putValue(String key, String value) {
        editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getValue(String key) {
        return sp.getString(key, null);
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public void moveValue(String key) {
        editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
}
