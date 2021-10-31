package com.streamlet.db;

import android.util.Log;

public class DbLog {

    private final static String TAG = "Database";
    public static boolean open = true;

    public static void switchLog(boolean isOpen) {
        DbLog.open = isOpen;
    }

    public static void log(String msg) {
        if (open) {
            Log.d(TAG, msg);
        }
    }
}
