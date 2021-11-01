package com.streamlet.db.sys;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.streamlet.db.client.DataBaseHelper;
import com.streamlet.db.client.DbLog;

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
        String[] dbList = context.databaseList();
        return Arrays.asList(dbList);
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

    /**
     * <p>
     * pragma table_info( 表名 )
     * </p>
     * 查询表结构
     * */
    public void queryTableStructure(String tableName){
        StringBuilder sb = new StringBuilder();
        sb.append("pragma table_info")
                .append("(")
                .append(tableName)
                .append(")");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        if (cursor.moveToFirst()){
            do {
                String[] columnNames = cursor.getColumnNames();
                DbLog.log(new Gson().toJson(columnNames));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
}
