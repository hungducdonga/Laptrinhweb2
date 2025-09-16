package com.example.spring_first_project.repository;

import com.example.spring_first_project.model.UserDemo;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserDemo, Integer>
{
    @NotNull List<UserDemo> findAll();
    UserDemo findByEmail(String email);
}