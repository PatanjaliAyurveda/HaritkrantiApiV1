package com.bharuwa.haritkranti.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * @author anuragdhunna
 */
public abstract class BaseObject {

    @Id
    private String id;

    @JsonIgnore
    private boolean deleted = false;

    @JsonIgnore
    private boolean updated = false;

    @JsonIgnore
    private  boolean isActive = false;

    @CreatedBy
    private String createdByUser;

    @Indexed
    @CreatedDate
    private Date creationDate = new Date();

//    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedUserId;

    private int schemaVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedUserId() {
        return lastModifiedUserId;
    }

    public void setLastModifiedUserId(String lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
}
