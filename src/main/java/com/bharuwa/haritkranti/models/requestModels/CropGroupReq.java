package com.bharuwa.haritkranti.models.requestModels;

import com.bharuwa.haritkranti.models.crops.CropGroup;

/**
 * @author anuragdhunna
 */
public class CropGroupReq {

    public CropGroupReq(String name, CropGroup.Type type) {
        this.name = name;
        this.type = type;
    }

    private String name;
    private CropGroup.Type type = CropGroup.Type.CROP;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CropGroup.Type getType() {
        return type;
    }

    public void setType(CropGroup.Type type) {
        this.type = type;
    }
}
