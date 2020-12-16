package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;

import static org.springframework.util.StringUtils.isEmpty;

public class Role extends BaseObject{

    private String description;
    @Indexed
    private String roleName;

    public Role(String roleName, String description) {
        super();
        this.roleName = roleName;
        this.description = description;
    }

    public Role() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        Role role = (Role) o;
        return !isEmpty(roleName) && this.roleName.equals(role.getRoleName());
    }

    @Override
    public int hashCode() {
        return !isEmpty(roleName) ? roleName.hashCode() : 0;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
