package com.example.spring_first_project.controller.restController;

import com.example.spring_first_project.model.Role;
import com.example.spring_first_project.repository.RoleRepository;
import com.example.spring_first_project.service.RoleServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RestRoleController
 * - Cung cấp REST API để quản lý và truy vấn thông tin Role (vai trò người dùng)
 */
@RestController
@RequestMapping("/api") // Tất cả các endpoint trong class này sẽ bắt đầu với /api
public class RestRoleController {

    private final RoleServiceImpl roleServiceImpl; // Service xử lý nghiệp vụ liên quan Role
    private final RoleRepository roleRepository;   // Repository truy xuất dữ liệu Role (chưa dùng trực tiếp trong code)

    // Constructor injection giúp dễ test và đảm bảo dependency bất biến
    public RestRoleController(RoleServiceImpl roleServiceImpl, RoleRepository roleRepository) {
        this.roleServiceImpl = roleServiceImpl;
        this.roleRepository = roleRepository;
    }

    /**
     * Lấy toàn bộ danh sách Role
     * GET /api/roles
     * @return Danh sách các Role trong hệ thống
     */
    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleServiceImpl.getAllRoles();
    }

    /**
     * Lấy thông tin chi tiết của một Role theo tên
     * GET /api/roles/{name}
     * @param name Tên Role (ví dụ: ROLE_ADMIN, ROLE_USER)
     * @return Role tương ứng hoặc null nếu không tồn tại
     */
    @GetMapping("/roles/{name}")
    public Role getRole(@PathVariable String name) {
        return roleServiceImpl.getRoleByName(name);
    }
}
