package com.bharuwa.haritkranti.models.crops;

import com.bharuwa.haritkranti.models.BaseObject;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author anuragdhunna
 */
@Document
public class CropGroup extends BaseObject {

    public CropGroup() {
    }

    public CropGroup(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public enum Type {
        CROP, FRUIT
    }

    private String name;

    private Type type = Type.CROP;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
