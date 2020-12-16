package com.bharuwa.haritkranti.models;

import java.util.Objects;

/**
 * @author anuragdhunna
 */
public class Locale {

    private String locale;  // en_US
    private String value;   // Wheat

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locale locale1 = (Locale) o;
        return locale.equals(locale1.locale) &&
                value.equals(locale1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locale, value);
    }
}
