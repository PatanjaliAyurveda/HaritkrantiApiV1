package com.bharuwa.haritkranti.models;

/**
 * @author anuragdhunna
 */
public class FieldSize extends BaseObject {

    public FieldSize(FieldSizeType fieldSizeType) {
        this.fieldSizeType = fieldSizeType;
    }
    public enum FieldSizeType {
        Acre, Hectare
    }

    private FieldSizeType fieldSizeType = FieldSizeType.Acre;

    public FieldSizeType getFieldSizeType() {
        return fieldSizeType;
    }

    public void setFieldSizeType(FieldSizeType fieldSizeType) {
        this.fieldSizeType = fieldSizeType;
    }
}
