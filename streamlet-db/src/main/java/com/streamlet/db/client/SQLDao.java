package com.streamlet.db.client;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLDao {

    private SQLiteDatabase db;

    private SQLDao(SQLiteDatabase db){
        this.db = db;
    }

    public static SQLDao getInstance(SQLiteDatabase db){
        return  new SQLDao(db);
    }


    /**
     * <p>
     * insert into configmenu2(baseurl, channelname,isdefault) values('http://','baotou','dfasfs')
     * </p>
     * @param entity:插入的记录
     * */
    public <T> void insert(T entity) {
        Class<?> clazz = entity.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ")
                .append(getTableName(clazz))
                .append(getTableFiled(clazz))
                .append(" values")
                .append(getInsertValue(entity));
        DbLog.log(sb.toString());
        if (isDbOpen()){
            db.execSQL(sb.toString());
        }
    }

    /**
     * <p>
     * update configmenu2 set channelname = 'beijing',isdefault = 'baotao' where id = 3
     * </p>
     * 更新所有字段，entity中空字段也更新
     * @param entity:需要更新的表记录
     * @param condition: where id = 3
     * */
    public <T> void update(T entity, String condition) {
        Class<?> clazz = entity.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append("update ")
                .append(getTableName(clazz))
                .append(" set ")
                .append(getUpdateContent(entity, false))
                .append(condition);
        DbLog.log(sb.toString());
        if (isDbOpen()){
            db.execSQL(sb.toString());
        }
    }

    /**
     * <p>
     * update configmenu2 set channelname = 'beijing',isdefault = 'baotao' where id = 3
     * </p>
     * 更新所有非空字段，entity中空字段不做处理
     * @param entity:需要更新的表记录
     * @param condition: where id = 3
     * */
    public <T> void updateActive(T entity, String condition) {
        Class<?> clazz = entity.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append("update ")
                .append(getTableName(clazz))
                .append(" set ")
                .append(getUpdateContent(entity, true))
                .append(condition);
        DbLog.log(sb.toString());
        if (isDbOpen()){
            db.execSQL(sb.toString());
        }
    }

    /**
     * @param clazz:需要查询的表
     * */
    public <T> List<T> queryAll(Class<?> clazz) {
        if (!isDbOpen()) return new ArrayList<>();
        String tableName = getTableName(clazz);
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        List<HashMap<String, Object>> queryResultMaps = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Field[] fields = clazz.getDeclaredFields();
                HashMap<String, Object> entity = new HashMap<>();
                for (Field field : fields) {
                    String typeName = field.getType().getSimpleName();
                    String fieldName = field.getName();
                    switch (typeName) {
                        case "int":
                        case "Integer":
                            int intField = cursor.getInt(cursor.getColumnIndex(fieldName));
                            entity.put(fieldName, intField);
                            break;
                        case "float":
                        case "Float":
                            float floatField = cursor.getFloat(cursor.getColumnIndex(fieldName));
                            entity.put(fieldName, floatField);
                            break;
                        case "double":
                        case "Double":
                            double doubleField = cursor.getDouble(cursor.getColumnIndex(fieldName));
                            entity.put(fieldName, doubleField);
                            break;
                        case "long":
                        case "Long":
                            long longField = cursor.getLong(cursor.getColumnIndex(fieldName));
                            entity.put(fieldName, longField);
                            break;
                        case "String":
                            String stringField = cursor.getString(cursor.getColumnIndex(fieldName));
                            entity.put(fieldName, stringField);
                            break;
                        default:
                            break;
                    }
                }
                queryResultMaps.add(entity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        String resultJSON = new Gson().toJson(queryResultMaps);
        DbLog.log(resultJSON);
        return new Gson().fromJson(resultJSON, new TypeToken<List<T>>() {}.getType());
    }

    /*
     * 获取表名
     */
    private String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null || "".equals(table.name())) return clazz.getSimpleName();
        return table.name();
    }

    /*获取表字段*/
    private static String getTableFiled(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (i == 0) {
                sb.append("(");
            }
            sb.append(fields[i].getName());
            if (i == fields.length - 1) {
                sb.append(")");
            } else {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /*获取插入值*/
    private static <T> String getInsertValue(T entity) {
        try {
            StringBuilder sb = new StringBuilder();
            Field[] fields = entity.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (i == 0) {
                    sb.append("(");
                }
                String filedType = fields[i].getType().getSimpleName();
                if (isNumber(filedType)) {
                    sb.append(fields[i].get(entity));
                } else {
                    sb.append("'");
                    sb.append(fields[i].get(entity));
                    sb.append("'");
                }
                if (i == fields.length - 1) {
                    sb.append(")");
                } else {
                    sb.append(",");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            DbLog.log(e.getMessage());
        }
        return "";
    }


    private static boolean isNumber(String filedType) {
        return "int".equals(filedType) || "Integer".equals(filedType) ||
                "float".equals(filedType) || "Float".equals(filedType) ||
                "double".equals(filedType) || "Double".equals(filedType) ||
                "long".equals(filedType) || "Long".equals(filedType);
    }

    /*
   更新字段
   isActive:字段值为null则不更新
   */
    private static <T> String getUpdateContent(T entity, boolean isActive) {
        try {
            StringBuilder sb = new StringBuilder();
            Class<?> clazz = entity.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String filedType = field.getName();
                Object value = field.get(entity);
                if (isActive && value == null) continue;
                sb.append(filedType).append("=");
                if (value == null) {
                    sb.append("null");
                } else {
                    if (isNumber(filedType)) {
                        sb.append(value);
                    } else {
                        sb.append("'");
                        sb.append(value);
                        sb.append("'");
                    }
                }
                sb.append(",");
            }
            return sb.substring(0, sb.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
            DbLog.log(e.getMessage());
        }
        return "";
    }

    private boolean isDbOpen(){
        if (db !=null && db.isOpen()){
            return true;
        }else{
            DbLog.log("database is null or is close!");
            return false;
        }
    }

}
