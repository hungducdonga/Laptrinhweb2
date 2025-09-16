package com.example.spring_first_project.repository;

import com.example.spring_first_project.model.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    @NotNull List<Role> findAll();
    Role findByAuthority(String authority);
}
