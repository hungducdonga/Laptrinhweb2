package com.example.spring_first_project.service;

import com.example.spring_first_project.model.Role;
import com.example.spring_first_project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    public void saveOrUpdate(Role role) {
        roleRepository.save(role);
    }
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    public Role getRoleById(int id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public Role getRoleByName(String roleName) {
        Role role = null;
        role = roleRepository.findByAuthority(roleName);
        return role;
    }

}
