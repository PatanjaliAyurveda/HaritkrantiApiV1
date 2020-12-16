package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author anuragdhunna
 */
@Document
public class App extends BaseObject{

    private String name;
    private int version;
    private int androidAppVersion;
    private int iosAppVersion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getAndroidAppVersion() {
        return androidAppVersion;
    }

    public void setAndroidAppVersion(int androidAppVersion) {
        this.androidAppVersion = androidAppVersion;
    }

    public int getIosAppVersion() {
        return iosAppVersion;
    }

    public void setIosAppVersion(int iosAppVersion) {
        this.iosAppVersion = iosAppVersion;
    }
}
