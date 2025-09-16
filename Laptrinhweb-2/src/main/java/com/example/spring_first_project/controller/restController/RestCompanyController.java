package com.example.spring_first_project.controller.restController;

import com.example.spring_first_project.dto.ApiResponse;
import com.example.spring_first_project.dto.CompanyApiDto;
import com.example.spring_first_project.model.Company;
import com.example.spring_first_project.service.CompanyServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RestCompanyController
 * - Cung cấp các REST API CRUD cho entity Company
 */
@RestController
@RequestMapping("/api") // Prefix chung cho tất cả endpoint trong controller này
public class RestCompanyController {

    private final CompanyServiceImpl companyService; // Service xử lý nghiệp vụ liên quan Company

    // Constructor injection (thay vì @Autowired) để dễ test và đảm bảo bất biến
    public RestCompanyController(CompanyServiceImpl companyService) {
        this.companyService = companyService;
    }

    /**
     * Lấy danh sách tất cả công ty
     * GET /api/companies
     */
    @GetMapping("/companies")
    public ResponseEntity<List<Company>> users() {
        List<Company> companies = companyService.getAllCompanies();
        System.out.println(companies); // Log ra console (có thể bỏ khi lên production)
        return ResponseEntity.ok(companies);
    }

    /**
     * Lấy thông tin chi tiết 1 công ty theo id
     * GET /api/company/{id}
     */
    @GetMapping("/company/{id}")
    public ResponseEntity<ApiResponse<Company>> findCompanyById(@PathVariable int id) {
        try {
            Company company = companyService.getCompanyById(id);
            if (company == null) {
                // Không tìm thấy công ty
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "Company not found", null));
            }
            return ResponseEntity.ok(new ApiResponse<>(200, "Company found", company));
        } catch (UsernameNotFoundException e) {
            // Trường hợp bị từ chối truy cập
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        } catch (Exception e) {
            // Lỗi hệ thống
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }

    /**
     * Tạo mới một công ty
     * POST /api/company
     */
    @PostMapping("/company")
    public ResponseEntity<ApiResponse<Company>> createCompany(@RequestBody CompanyApiDto company) {
        try {
            Company newCompany = companyService.saveOrUpdate(company);
            if (newCompany == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "Company not found", null));
            }
            return ResponseEntity.ok(new ApiResponse<>(200, "Company just creation", newCompany));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }

    /**
     * Xóa công ty theo id
     * DELETE /api/company/{id}
     * - Không trả về ResponseEntity vì chỉ cần xóa (204 No Content)
     */
    @DeleteMapping("/company/{id}")
    public void deleteCompanyById(@PathVariable int id) {
        companyService.deleteCompanyById(id);
    }

    /**
     * Cập nhật thông tin công ty theo id
     * PUT /api/company/{id}
     */
    @PutMapping("/company/{id}")
    public ResponseEntity<ApiResponse<Company>> updateCompanyById(@PathVariable int id,
                                                                  @RequestBody CompanyApiDto company) {
        try {
            Company updateCompany = companyService.updateCompany(id, company);
            if (updateCompany == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "Company not found", null));
            }
            return ResponseEntity.ok(new ApiResponse<>(200, "Company just updated", updateCompany));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }
}
