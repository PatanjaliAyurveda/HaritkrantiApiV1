package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.repositories.RoleRepo;
import com.bharuwa.haritkranti.models.Role;
import com.bharuwa.haritkranti.service.RoleService;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    GenericMongoTemplate genericMongoTemplate;

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public List<Role> listAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Role findById(String id) {
        return genericMongoTemplate.findById(id, Role.class);
    }

    @Override
    public Role roleName(String roleName) {
        return roleRepo.findByName(roleName);
    }

    @Override
    public void createDefaultRoles(Role role) {
        if (roleRepo.findByName(role.getRoleName()) == null) {
            saveRole(role);
        }
    }
}