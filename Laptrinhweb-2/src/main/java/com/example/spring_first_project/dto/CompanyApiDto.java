package com.example.spring_first_project.dto;

public class CompanyApiDto {

    private String companyName;

    public CompanyApiDto(String companyName) {
        this.companyName = companyName;
    }

    public CompanyApiDto() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
