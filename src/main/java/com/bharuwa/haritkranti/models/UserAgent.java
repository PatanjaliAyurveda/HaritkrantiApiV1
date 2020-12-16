package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author harmanpreet
 */
public class UserAgent extends BaseObject{

    @Indexed(unique = true)
    private String agentId;

    @DBRef
    private Set<User> users;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        if (user == null) return;
        if (getUsers() == null) this.users = new LinkedHashSet<>();
        this.users.add(user);
    }

    public void removeUser(User user) {
        if (user == null || getUsers() == null || getUsers().isEmpty()) return;
        getUsers().remove(user);
    }

}
