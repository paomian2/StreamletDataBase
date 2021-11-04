package com.streamlet.db.sys;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.streamlet.db.client.DataBaseHelper;
import com.streamlet.db.client.DbLog;
import com.streamlet.db.utils.GsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 查看数据库的工具
 */
public class SQLExport {

    private final SQLiteDatabase db;

    private SQLExport(SQLiteDatabase db) {
        this.db = db;
    }

    public static SQLExport getInstance(SQLiteDatabase db) {
        return new SQLExport(db);
    }

    public static SQLExport getInstance(Context context, String dbName) {
        return new SQLExport(DataBaseHelper.getInstance(context, dbName).getWritableDatabase());
    }

    public static List<String> getDBList(Context context) {
        String[] dbList = context.databaseList();
        return Arrays.asList(dbList);
    }

    /**
     * 查询数控的所有表
     */
    public List<String> queryTables() {
        String sql = "SELECT name FROM sqlite_master";
        Cursor cursor = db.query("sqlite_master", null, null, null, null, null, null);
        List<String> tables = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(cursor.getColumnIndex("name"));
                tables.add(tableName);
            } while (cursor.moveToNext());
        }
        return tables;
    }


    public <T> List<T> queryTableStructure(String tableName, Class<T> clazz) {
        List<HashMap<String, Object>> tableAttrs = queryTableStructure(tableName);
        String resultListJSON = new Gson().toJson(tableAttrs);
        return GsonUtils.json2List(resultListJSON, clazz);
    }

    /**
     * 查询表结构
     * <p>
     * pragma table_info( 表名 )
     * </p>
     * cid  name type notnull dflt_value pk
     */
    private List<HashMap<String, Object>> queryTableStructure(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("pragma table_info")
                .append("(")
                .append(tableName)
                .append(")");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List<HashMap<String, Object>> tableAttrs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int cid = cursor.getInt(cursor.getColumnIndex("cid"));
                String fieldName = cursor.getString(cursor.getColumnIndex("name"));
                String fieldType = cursor.getString(cursor.getColumnIndex("type"));
                int primary = cursor.getInt(cursor.getColumnIndex("pk"));
                int notNull = cursor.getInt(cursor.getColumnIndex("notnull"));
                String defValue = cursor.getString(cursor.getColumnIndex("dflt_value"));
                HashMap<String, Object> entity = new HashMap<>();
                entity.put("cid", cid);
                entity.put("fieldName", fieldName);
                entity.put("fieldType", fieldType);
                entity.put("primary", primary == 1);
                entity.put("notNull", notNull == 1);
                entity.put("defValue", defValue);
                tableAttrs.add(entity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        String resultListJSON = new Gson().toJson(tableAttrs);
        DbLog.log(resultListJSON);
        return tableAttrs;
    }


    /**
     * 查询外键列表
     * <p>
     * pragma foreign_key_list(表名);
     * </p>
     * id seq table(外键对应的表) from(需要添加外健的字段) to(外键的字段) on_update(更新动作) on_delete(删除动作) match
     */
    public <T> List<T> foreignKeyList(String tableName, Class<T> clazz) {
        return new ArrayList<>();
    }

    public List<LinkedHashMap<String, Object>> queryAll(String tableName) {
        List<HashMap<String, Object>> attrsList = queryTableStructure(tableName);
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        List<LinkedHashMap<String, Object>> queryResultMaps = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                LinkedHashMap<String, Object> entity = new LinkedHashMap<>();
                for (HashMap<String, Object> field : attrsList) {
                    String typeName = field.get("fieldType") + "";
                    String fieldName = field.get("fieldName") + "";
                    if (typeName.contains("int") || typeName.contains("INT") || typeName.contains("integer") || typeName.contains("INTEGER")){
                        int intField = cursor.getInt(cursor.getColumnIndex(fieldName));
                        entity.put(fieldName, intField);
                    }else if (typeName.contains("float") || typeName.contains("Float")){
                        float floatField = cursor.getFloat(cursor.getColumnIndex(fieldName));
                        entity.put(fieldName, floatField);
                    }else if (typeName.contains("double") || typeName.contains("DOUBLE")){
                        double doubleField = cursor.getDouble(cursor.getColumnIndex(fieldName));
                        entity.put(fieldName, doubleField);
                    }else if (typeName.contains("long") || typeName.contains("Long")){
                        long longField = cursor.getLong(cursor.getColumnIndex(fieldName));
                        entity.put(fieldName, longField);
                    }else if (typeName.contains("varchar") || typeName.contains("string") ||
                              typeName.contains("String")  || typeName.contains("STRING")  || typeName.contains("TEXT")){
                        String stringField = cursor.getString(cursor.getColumnIndex(fieldName));
                        entity.put(fieldName, stringField);
                    }else if (typeName.contains("char") || typeName.contains("CHAR")){
                       //TODO
                    }else if (typeName.contains("boolean") || typeName.contains("BOOLEAN")){
                        //TODO
                    }else if (typeName.contains("numeric") || typeName.contains("NUMERIC")){
                        //TODO
                    }else if (typeName.contains("decimal") || typeName.contains("DECIMAL")){
                        //TODO
                    }else if (typeName.contains("date") || typeName.contains("DATE")){
                        //TODO
                    }else if (typeName.contains("datetime") || typeName.contains("DATETIME")){
                        //TODO
                    }else if (typeName.contains("time") || typeName.contains("TIME")){
                        //TODO
                    }else if (typeName.contains("real") || typeName.contains("REAL")){

                    }
                }
                queryResultMaps.add(entity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        String resultJSON = new Gson().toJson(queryResultMaps);
        DbLog.log(resultJSON);
        return queryResultMaps;
    }


    /**
     * 查看表的约束(PRIMARY_KEY AUTOINCREMENT、NOTNULL、UNIQUE、DEFAULT、CHECK)
     * <P>
     *  pragma table_info(表名称); cid  name type notnull dflt_value pk
     *  pragma index_list(表名称); seq name(索引名称) unique(1/0) origin(u:unique索引,c:生序索引) partial(条件)
     *  pragma index_info(索引名称); seqno cid name(字段名称)
     * </P>
     * */
    public List<HashMap<String,Object>> getConstraintList(String tableName){
        List<HashMap<String,Object>> rows = new ArrayList<>();
        List<String> uniqueFieldList = getUniqueFieldList(tableName);
        List<HashMap<String ,Object>> attrs = queryTableStructure(tableName);
        for (HashMap<String ,Object> entity:attrs){
            if (Boolean.parseBoolean(entity.get("primary")+"")){
                HashMap<String,Object> itemBean = new HashMap<>();
                itemBean.put("Score",entity.get("fieldName"));
                itemBean.put("Type","PRIMARY KEY");
                itemBean.put("name","");
                itemBean.put("detail","");
                rows.add(itemBean);
                //是否递增
            }
            if (Boolean.parseBoolean(entity.get("notNull")+"")){
                HashMap<String,Object> itemBean = new HashMap<>();
                itemBean.put("Score","Column("+entity.get("fieldName")+")");
                itemBean.put("Type","NOTNULL");
                itemBean.put("name","");
                itemBean.put("detail","");
                rows.add(itemBean);
            }
            if (uniqueFieldList.contains(entity.get("name")+"")){
                HashMap<String,Object> itemBean = new HashMap<>();
                itemBean.put("Score","Column("+entity.get("fieldName")+")");
                itemBean.put("Type","UNIQUE");
                itemBean.put("name","");
                itemBean.put("detail","");
                rows.add(itemBean);
            }
        }
        DbLog.log(new Gson().toJson(rows));
        return rows;
    }

    /**
     * unique本身就是索引
     * 索引不一定是unique
     * */
    public List<String> getUniqueFieldList(String tableName){
        List<String> fieldList = new ArrayList<>();
        List<HashMap<String,Object>> indexesList = getIndexesList(tableName);
        for (HashMap<String ,Object> entity:indexesList){
            if ("1".equals(entity.get("unique")+"")){
                List<HashMap<String ,Object>> info = getIndexesInfo(entity.get("name")+"");
                for (HashMap<String ,Object> hashMap:info){
                    fieldList.add(hashMap.get("name")+"");
                }
            }
        }
        return fieldList;
    }

    /**
     * 查询所有存在索引的字段的索引详情
     * */
    public List<HashMap<String ,Object>> getAllIndexesInfoList(String tableName){
        List<HashMap<String,Object>> indexesList =getIndexesList(tableName);
        for (HashMap<String ,Object> entity :indexesList){
            List<HashMap<String ,Object>> indexesInfo = getIndexesInfo(entity.get("name")+"");
            entity.put("info",indexesInfo);
        }
        DbLog.log(new Gson().toJson(indexesList));
        return indexesList;
    }


    /**
     * <p>
     * index_list(表名); seq name(索引名称) unique(1/0) origin partial
     * </p>
     * 1.unique本身就是索引
     * 2.可以自定义索引eg:
     * CREATE INDEX 自定义索引名称 ON 表名称 (
     *     要添加索引的字段 ASC
     * );
     * */
    public List<HashMap<String,Object>> getIndexesList(String tableName){
        StringBuilder sb = new StringBuilder();
        sb.append("pragma index_list")
                .append("(")
                .append(tableName)
                .append(")");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List<HashMap<String, Object>> indexesList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int seq = cursor.getInt(cursor.getColumnIndex("seq"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int unique = cursor.getInt(cursor.getColumnIndex("unique"));
                String origin = cursor.getString(cursor.getColumnIndex("origin"));
                int partial = cursor.getInt(cursor.getColumnIndex("partial"));
                HashMap<String, Object> entity = new HashMap<>();
                entity.put("seq", seq);
                entity.put("name", name);
                entity.put("unique", unique);
                entity.put("origin", origin);
                entity.put("partial", partial);
                indexesList.add(entity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DbLog.log(new Gson().toJson(indexesList));
        return indexesList;
    }

    /**
     * <p>
     * pragma index_info(索引名称); seqno cid name(字段名称)
     * </p>
     * 多个字段可以定义同一个名字的索引，所以返回的是列表
     * */
    public List<HashMap<String ,Object>> getIndexesInfo(String indexesName){
        List<HashMap<String ,Object>> indexInfoFields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("pragma index_info")
                .append("(")
                .append(indexesName)
                .append(")");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> indexesInfo = new HashMap<>();
                int seqno = cursor.getInt(cursor.getColumnIndex("seqno"));
                int cid = cursor.getInt(cursor.getColumnIndex("cid"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                HashMap<String, Object> entity = new HashMap<>();
                indexesInfo.put("seqno", seqno);
                indexesInfo.put("cid", cid);
                indexesInfo.put("name", name);
                indexInfoFields.add(indexesInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DbLog.log(new Gson().toJson(indexInfoFields));
        return indexInfoFields;
    }



    /**
     * CREATE TABLE questionbank (
     *     id           INTEGER       PRIMARY KEY AUTOINCREMENT
     *                                NOT NULL,
     *     question     TEXT          NOT NULL
     *                                UNIQUE
     *                                DEFAULT nihao,
     *     interview_id VARCHAR (255) UNIQUE,
     *     category_id  TEXT,
     *     optiona      TEXT,
     *     optionb      TEXT,
     *     optionc      TEXT,
     *     optiond      TEXT,
     *     anwser       TEXT,
     *     type         TEXT,
     *     url          TEXT,
     *     link_ids     TEXT          UNIQUE
     * );
     * 获取建表DDL
     *
     * SQLite中，AUTOINCREMENT一定是主键，主键是INTEGER一定是AUTOINCREMENT
     * 没用查询AUTOINCREMENT的api，select * from sqlite_sequence;只能查出有AUTOINCREMENT对应的表跟seq，但找不到对应的字段
     * */
    public String queryCreateTableDDL(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE")
                .append(" ")
                .append(tableName)
                .append("(")
                .append("\n");
        //表结构
        List<HashMap<String, Object>> structureList = queryTableStructure(tableName);
        //unique字段列表
        List<String> uniqueFieldList = getUniqueFieldList(tableName);
        for (int i = 0;i<structureList.size();i++) {
            HashMap<String, Object> entity = structureList.get(i);
            sb.append(" ")
              .append(" ");
            sb.append(" ").append(entity.get("fieldName"))
              .append(" ").append(entity.get("fieldType"));
            if (Boolean.parseBoolean(entity.get("primary")+"")){
                sb.append(" ").append("PRIMARY KEY");
                //查看是否递增
                String typeName = entity.get("fieldType")+"";
                if (typeName.contains("int") || typeName.contains("INT") || typeName.contains("integer") || typeName.contains("INTEGER")){
                    sb.append(" ").append("AUTOINCREMENT");
                }
            }
            if (Boolean.parseBoolean(entity.get("notNull")+"")){
                sb.append(" ").append("NOT NULL");
            }
            for (String field:uniqueFieldList){
                if (field.equals(entity.get("fieldName"))){
                    sb.append(" ").append("UNIQUE");
                }
            }
            if (entity.get("defValue")!=null){
                sb.append(" ").append("DEFAULT").append(" ").append(entity.get("defValue"));
            }
            if (i != structureList.size()-1){
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append(");");
        DbLog.log(sb.toString());
        return sb.toString();
    }
}
