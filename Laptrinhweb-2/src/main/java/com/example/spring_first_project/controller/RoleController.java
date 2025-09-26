package com.example.spring_first_project.controller;

import com.example.spring_first_project.model.Role;
import com.example.spring_first_project.service.RoleServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * RoleController
 * --------------
 * Quản lý các thao tác CRUD đơn giản cho bảng Role (vai trò/người dùng).
 * Sử dụng mô hình Spring MVC với view template (Thymeleaf/JSP).
 */
@Controller
public class RoleController {

    // Service xử lý logic và truy xuất dữ liệu Role
    private final RoleServiceImpl roleService;

    /**
     * Constructor injection:
     * Spring sẽ tự động cung cấp đối tượng RoleServiceImpl khi khởi tạo controller.
     */
    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    /**
     * Hiển thị danh sách Role
     * URL: GET /role
     * - Lấy toàn bộ roles từ DB và đẩy vào model để view hiển thị.
     */
    @GetMapping("/role")
    public String role(Model model) {
        List<Role> roles = roleService.getAllRoles();   // Lấy danh sách từ DB
        model.addAttribute("roles", roles);            // Đưa vào model để render
        return "role";                                 // Trả về template role.html
    }

    /**
     * Hiển thị form thêm Role mới
     * URL: GET /addRole
     * - Chuẩn bị 1 đối tượng Role rỗng để bind vào form.
     */
    @GetMapping("/addRole")
    public String addRole(Model model) {
        model.addAttribute("role", new Role());        // Tạo Role trống cho form
        return "addRole";                              // Trả về template addRole.html
    }

    /**
     * Xử lý khi submit form thêm Role
     * URL: POST /addRole
     * - Lưu role mới vào DB
     * - Redirect về trang danh sách Role.
     */
    @PostMapping("addRole")
    public String addRole(@ModelAttribute("role") Role role,
                          RedirectAttributes redirectAttributes) {
        // Lưu hoặc cập nhật Role xuống DB
        roleService.saveOrUpdate(role);

        // Có thể thêm thông báo flash nếu cần:
        // redirectAttributes.addFlashAttribute("message", "Thêm vai trò thành công");

        // Chuyển hướng về danh sách Role
        return "redirect:/role";
    }
}
