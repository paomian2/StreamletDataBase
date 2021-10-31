package com.streamlet.db.sys;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.streamlet.db.DataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 查看数据库的工具
 * */
public class SQLExport {

    private final SQLiteDatabase db;

    private SQLExport(SQLiteDatabase db){
        this.db = db;
    }

    public static SQLExport getInstance(SQLiteDatabase db){
        return new SQLExport(db);
    }

    public static SQLExport getInstance(Context context,String dbName){
        return new SQLExport(DataBaseHelper.getInstance(context,dbName).getWritableDatabase());
    }

    public static List<String> getDBList(Context context){
        return Arrays.asList(context.databaseList().clone());
    }

    /**
     * 查询数控的所有表
     * */
    public List<String> queryTables(){
        String sql = "SELECT name FROM sqlite_master";
        Cursor cursor = db.query("sqlite_master",null,null,null,null,null,null);
        List<String> tables = new ArrayList<>();
        if ( cursor.moveToFirst()){
            do {
                String tableName = cursor.getString(cursor.getColumnIndex("name"));
                tables.add(tableName);
            }while (cursor.moveToNext());
        }
        return tables;
    }

}
