package com.streamlet.db.client;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context,
                          @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static DataBaseHelper getInstance(Context context,
                                             String dbName,
                                             int dbVersion) {
        return new DataBaseHelper(context, dbName, null, dbVersion);
    }

    public static DataBaseHelper getInstance(Context context,
                                             String dbName) {
        return new DataBaseHelper(context, dbName, null, 1);
    }

    public static SQLiteDatabase createDataBase(Context context,
                                             String dbName) {
        return new DataBaseHelper(context, dbName, null, 1).getWritableDatabase();
    }



    public void createTable(Class<?> clazz) {
        Generator.createTable(this.getReadableDatabase(), clazz);
    }

    public <T> void insert(T entity) {
        SQLDao.getInstance(this.getWritableDatabase()).insert(entity);
    }

    public <T> void update(T entity) {
        SQLDao.getInstance(this.getWritableDatabase()).update(entity, "");
    }

    public <T> void updateActive(T entity) {
        SQLDao.getInstance(this.getWritableDatabase()).updateActive(entity, "");
    }

    public <T> List<T> queryAll(Class<T> clazz) {
        return SQLDao.getInstance(this.getWritableDatabase()).queryAll(clazz);
    }

    public void executeSQL(String sql) {
        try {
            if (this.getWritableDatabase().isOpen()) {
                this.getWritableDatabase().execSQL(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DbLog.log(e.getMessage());
        }
    }
}

