package com.example.spring_first_project.service;

import com.example.spring_first_project.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    void saveOrUpdate(Role role);
    List<Role> getAllRoles();
    Role getRoleById(int id);
    Role getRoleByName(String roleName);
}
