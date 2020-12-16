package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.models.Role;
import com.bharuwa.haritkranti.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ResponseBody
    public Role addRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }

    @RequestMapping(value = "/getAllRoles", method = RequestMethod.GET)
    @ResponseBody
    public List<Role> getAllRoles() {
        return roleService.listAllRoles();
    }

    @RequestMapping(value = "/getRoleById", method = RequestMethod.GET)
    @ResponseBody
    public Role getRoleById(@RequestParam String id) {
        return roleService.findById(id);
    }
}