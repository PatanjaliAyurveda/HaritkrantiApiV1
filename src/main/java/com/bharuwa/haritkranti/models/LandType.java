package com.bharuwa.haritkranti.models;

/**
 * @author anuragdhunna
 */
public class LandType {

    public LandType(Type type, int range) {
        this.type = type;
        this.range = range;
    }

    public enum Type {
        Irrigated, SemiIrrigated, Rainfed
    }

    private Type type = Type.Irrigated;
    private int range;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
