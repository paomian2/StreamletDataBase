package com.streamlet.db.bean;

/**
 * @author Linxz
 * 创建日期：2021年11月01日 6:09 下午
 * version：
 * 描述：
 */
public class TableAttrs {

    private String fieldName;
    private String fieldType;
    private int size;
    private boolean primary;
    private boolean outKey;
    private boolean unique;
    private boolean notNull;
    private boolean autoincrement;
    private Object value;
    private Object defValue;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isOutKey() {
        return outKey;
    }

    public void setOutKey(boolean outKey) {
        this.outKey = outKey;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }

    public void setAutoincrement(boolean autoincrement) {
        this.autoincrement = autoincrement;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getDefValue() {
        return defValue;
    }

    public void setDefValue(Object defValue) {
        this.defValue = defValue;
    }
}