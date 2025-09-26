package com.example.spring_first_project.repository;

import com.example.spring_first_project.model.Company;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository cho Entity Company
 * - Kế thừa từ CrudRepository để cung cấp các hàm CRUD mặc định
 * - Được Spring Data JPA tự động triển khai, không cần viết code SQL.
 */
@Repository
public interface CompanyRepository extends CrudRepository<Company, Integer> {

    /**
     * Lấy danh sách tất cả Company.
     * - Ghi đè lại phương thức findAll của CrudRepository
     * - @NotNull: đảm bảo không trả về null.
     */
    @NotNull
    List<Company> findAll();

    /**
     * Tìm Company theo tên công ty.
     * - Spring Data JPA sẽ tự động tạo truy vấn SQL từ tên hàm (findBy + tên trường)
     * - Ví dụ: SELECT * FROM company WHERE company_name = ?;
     */
    Company findByCompanyName(String companyName);
}

