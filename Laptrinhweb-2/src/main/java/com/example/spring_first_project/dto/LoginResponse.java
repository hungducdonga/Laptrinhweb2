package com.example.spring_first_project.dto;

public class LoginResponse {

    private UserLoginDto userLoginDto;
    private String role;
    private String token;
    private String email;

    public LoginResponse(UserLoginDto userLoginDto, String token, String role, String email) {
        this.userLoginDto = userLoginDto;
        this.token = token;
        this.role = role;
        this.email = email;
    }

    public LoginResponse() {
    }

    public UserLoginDto getUserLoginDto() {
        return userLoginDto;
    }

    public void setUserLoginDto(UserLoginDto userLoginDto) {
        this.userLoginDto = userLoginDto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
