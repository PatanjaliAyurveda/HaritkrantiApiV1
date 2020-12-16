package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.Role;

import java.util.List;

public interface RoleService {

    Role saveRole(Role role);

    List<Role> listAllRoles();

    Role findById(String id);

    Role roleName(String roleName);

    void createDefaultRoles(Role role);
}
