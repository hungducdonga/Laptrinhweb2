package com.example.spring_first_project.repository;

import com.example.spring_first_project.model.Company;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Integer> {
    @NotNull List<Company> findAll();
    Company findByCompanyName(String companyName);
}
