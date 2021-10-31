package com.streamlet.db;

import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
/*
 * 1.创建数据库
 * 2.数据库版本控制
 * 3.创建表
 * 4.更新表
 * 5.添加记录
 * 6.查询记录
 * 7.更新记录
 * 8.删除记录
 * */
public class Generator {

    /**
     * <p>
     * create table user(id integer,name varchar(255));
     * </p>
     * */
    public static void createTable(SQLiteDatabase db,Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sqlSB = new StringBuilder();
        for (Field f : fields) {
            String sqlFiledStr = "";
            String type =f.getType().getSimpleName();
            String name = f.getName();
            if ("int".equals(type)) {
                if (isId(f)){
                    sqlFiledStr = "integer";
                }else{
                    sqlFiledStr = "int("+generateFieldLength(f)+")";
                }
            } else if ("String".equals(type)) {
                sqlFiledStr = "varchar("+generateFieldLength(f)+")";
            }
            sqlFiledStr += generateId(f);
            sqlFiledStr += generatePrimaryKey(f);
            sqlSB.append(name).append(" ").append(sqlFiledStr).append(",");
        }

        String sqlFiled = sqlSB.substring(0, sqlSB.length() - 1);
        String sqlStr = "create table if not exists " +
                generateTableName(clazz) +
                "(" +
                sqlFiled +
                ")";
        DbLog.log(sqlStr);
        db.execSQL(sqlStr.toString());
    }

    private static String generateTableName(Class<?> clazz){
        Table table = clazz.getAnnotation(Table.class);
        if (table == null || "".equals(table.name())) return clazz.getSimpleName();
        return table.name();
    }

    private static boolean isId(Field field) {
        Id id = field.getAnnotation(Id.class);
        return id != null;
    }

    private static String generateId(Field field){
        field.setAccessible(true);
        StringBuilder sb = new StringBuilder();
        Id id= field.getAnnotation(Id.class);
        if (id == null) return "";
        if (id.primaryKey()){
            sb.append(" ").append("primary key");
        }
        if (id.autoincrement()){
            sb.append(" ").append("autoincrement");
        }
        return sb.toString();
    }

    /*
     * 只允许一个字段有主键
     */
    private static String generatePrimaryKey(Field field){
        field.setAccessible(true);
        PrimaryKey primaryKey= field.getAnnotation(PrimaryKey.class);
        if (primaryKey == null) return "";
        return " primary key";
    }

    private static int generateFieldLength(Field field){
        Length length = field.getAnnotation(Length.class);
        if (length == null) return 255;
        return length.value();
    }
}
