package com.bharuwa.haritkranti.config;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
public class ClientDomain {

    private final String name;

    private final String surname;

    public ClientDomain(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
