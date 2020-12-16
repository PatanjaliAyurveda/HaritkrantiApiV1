package com.bharuwa.haritkranti.models.location;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * @author harman
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "village_khasra", unique = true, def = "{'khasraNo' : 1, 'village': 1}")
})
public class KhasraNumber {

    @NotNull(message = "Khasra number must not be null")
    private String khasraNo;

    @DBRef
    private Village village;

    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }
}
