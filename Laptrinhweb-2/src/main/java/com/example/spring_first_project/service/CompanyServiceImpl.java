package com.example.spring_first_project.service;

import com.example.spring_first_project.dto.CompanyApiDto;
import com.example.spring_first_project.model.Company;
import com.example.spring_first_project.model.UserDemo;
import com.example.spring_first_project.repository.CompanyRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService{

    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company saveOrUpdate(CompanyApiDto companyApiDto) {
        Company company = getCompanyByCompanyName(companyApiDto.getCompanyName());
        Company savedCompany;
        if (company == null) {
            company = new Company();
            company.setCompanyName(companyApiDto.getCompanyName());
            savedCompany = companyRepository.save(company);
        }else {
            throw new RuntimeException("Company with name '" + companyApiDto.getCompanyName() + " already exists");
        }
        return savedCompany;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName);
    }

    public Company getCompanyById(int id) {
        return companyRepository.findById(id).orElse(null);
    }

    public Company updateCompany(int id, @NotNull CompanyApiDto companyApiDto) {
        Company company = getCompanyById(id);
        if (company != null) {
            company.setCompanyName(companyApiDto.getCompanyName());
            companyRepository.save(company);
        }else {
            throw new RuntimeException("Company with name '" + companyApiDto.getCompanyName() + " does not exist");
        }
        return company;
    }
    public void deleteCompanyById(int id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null && company.getUsers() != null) {
            for (UserDemo user : company.getUsers()) {
                user.setCompany(null); // Hủy liên kết giữa UserDemo và Company
            }
        }
        companyRepository.deleteById(id);
    }
}
