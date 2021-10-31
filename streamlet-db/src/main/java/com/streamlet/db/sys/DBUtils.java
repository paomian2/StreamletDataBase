package com.streamlet.db.sys;

import android.content.Context;

import com.streamlet.db.DbLog;


public class DBUtils {

    public static void getDbList(Context context){
        String[] dbs = context.databaseList();
        for (String db:dbs){
            DbLog.log(db);
        }
    }

}
