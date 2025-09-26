package com.example.spring_first_project.controller;

import com.example.spring_first_project.dto.CompanyApiDto;
import com.example.spring_first_project.model.Company;
import com.example.spring_first_project.service.CompanyServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CompanyController
 * -----------------
 * Controller xử lý các yêu cầu (request) liên quan đến đối tượng Company.
 * Sử dụng mô hình MVC truyền thống với Thymeleaf/JSP (trả về tên view thay vì JSON).
 */
@Controller
public class CompanyController {

    private final CompanyServiceImpl companyService;

    // Constructor Injection: Spring sẽ tự inject CompanyServiceImpl
    public CompanyController(CompanyServiceImpl companyService) {
        this.companyService = companyService;
    }

    /**
     * Trang danh sách công ty
     * URL: GET /company
     * Lấy toàn bộ danh sách công ty và hiển thị ra view "company".
     */
    @GetMapping("/company")
    public String trangChiTiet(@ModelAttribute("userName") String userName, Model model) {
        // Lấy danh sách công ty từ DB
        List<Company> list = companyService.getAllCompanies();
        // Truyền danh sách sang view qua model
        model.addAttribute("company", list);
        return "company"; // Tên view (VD: company.html trong templates)
    }

    /**
     * Hiển thị form thêm mới công ty
     * URL: GET /addCompany
     */
    @GetMapping("/addCompany")
    public String addCompany(Model model) {
        model.addAttribute("company", new Company()); // Gửi object trống để binding form
        return "addCompany"; // View hiển thị form nhập
    }

    /**
     * Xử lý submit form thêm công ty
     * URL: POST /addCompany
     */
    @PostMapping("addCompany")
    public String addCompany(@ModelAttribute("company") CompanyApiDto company) {
        // Lưu dữ liệu công ty mới
        companyService.saveOrUpdate(company);
        // Sau khi lưu, quay lại view danh sách (company.html)
        return "company";
    }

    /**
     * Hiển thị form cập nhật thông tin công ty
     * URL: GET /updateCompany/{id}
     * {id}: id công ty cần sửa
     */
    @GetMapping("/updateCompany/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) {
        Company company = companyService.getCompanyById(id);
        model.addAttribute("company", company);
        return "updateCompany"; // Trả về form cập nhật
    }

    /**
     * Xử lý cập nhật thông tin công ty
     * URL: POST /updateCompany/{id}
     */
    @PostMapping("/updateCompany/{id}")
    public String updateCompany(@PathVariable int id,
                                @ModelAttribute("company") CompanyApiDto company) {
        companyService.updateCompany(id, company);
        // Redirect về danh sách sau khi update
        return "redirect:/company";
    }

    /**
     * Xóa công ty theo id
     * URL: GET /deleteCompany/{id}
     */
    @GetMapping("/deleteCompany/{id}")
    public String deleteCompany(@PathVariable("id") int id) {
        companyService.deleteCompanyById(id);
        // Quay lại trang danh sách công ty
        return "redirect:/company";
    }
}
