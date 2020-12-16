package com.bharuwa.haritkranti.models.responseModels;

import com.bharuwa.haritkranti.models.User;

import java.util.Set;

/**
 * @author harman
 */
public class OCEligibleUsersAndCount {

    private Set<User> users;

    private int count;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
