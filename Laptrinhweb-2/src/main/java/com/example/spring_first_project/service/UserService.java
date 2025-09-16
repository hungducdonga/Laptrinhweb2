package com.example.spring_first_project.service;

import com.example.spring_first_project.dto.*;
import com.example.spring_first_project.model.Role;
import com.example.spring_first_project.model.UserDemo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


@Service
public interface UserService extends UserDetailsService {
    LoginResponse login(UserLoginDto userLoginDto, String token);
    List<UserDemo> findAll();
    void save(UserRegistrationDto userRegistrationDto);
    UserDemo saveUserWithApi(UserRegistrationApiDto userRegistrationApiDto);
    UserDemo updateUserWithApi(UserUpdateApiDto userUpdateApiDto, int id);
    UserDemo decentralizationWithApi(DecentralizationDto decentralizationDto, int id);
    void deleteUserWithApi(int id);
    UserDemo findByUsername(String username);
    UserDemo getUserById(int id);
    Boolean existedEmailChecking(String email);
    Collection<Role> getUserRoles(int id);
}