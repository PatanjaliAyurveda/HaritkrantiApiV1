package com.bharuwa.haritkranti.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author anuragdhunna
 */
@Document(collection = "customSequences")
public class CustomSequences {

    @Id
    private String id;
    private long seq;

    public CustomSequences(String id, long seq) {
        this.id = id;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }
}
