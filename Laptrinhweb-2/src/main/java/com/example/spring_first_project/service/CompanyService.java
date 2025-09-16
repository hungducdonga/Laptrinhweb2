package com.example.spring_first_project.service;

import com.example.spring_first_project.dto.CompanyApiDto;
import com.example.spring_first_project.model.Company;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface CompanyService {

    Company saveOrUpdate(CompanyApiDto companyApiDto);
    List<Company> getAllCompanies();
    Company getCompanyByCompanyName(String companyName);
    Company getCompanyById(int id);
    void deleteCompanyById(int id);
    Company updateCompany(int id, @Nonnull CompanyApiDto companyApiDto);
}
