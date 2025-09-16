package com.example.spring_first_project.dto;

import java.util.List;

public class DecentralizationDto {

    private List<String> authorities;

    public DecentralizationDto(List<String> authorities) {
        this.authorities = authorities;
    }

    public DecentralizationDto() {
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
