package com.example.spring_first_project.dto;

import java.util.List;

/**
 * UserUpdateApiDto
 * ------------------------------------------------------
 * DTO dành cho API cập nhật thông tin người dùng.
 * Dùng khi client gửi dữ liệu PUT/PATCH để sửa user.
 * Bao gồm thông tin cơ bản, công ty và danh sách quyền.
 */
public class UserUpdateApiDto {

    /** Tên (first name) của người dùng */
    private String firstName;

    /** Họ (last name) của người dùng */
    private String lastName;

    /** Địa chỉ email của người dùng (định danh login) */
    private String email;

    /**
     * Mật khẩu mới (nếu cần đổi).
     * Có thể để trống khi không cập nhật password.
     */
    private String password;

    /** ID công ty mà người dùng thuộc về */
    private int company;

    /**
     * Danh sách tên quyền (roles/authorities),
     * ví dụ ["ROLE_USER","ROLE_ADMIN"].
     */
    private List<String> authorities;

    /** Constructor đầy đủ để khởi tạo nhanh từ request */
    public UserUpdateApiDto(
            String firstName,
            String lastName,
            String email,
            String password,
            int company,
            List<String> authorities) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.company = company;
        this.authorities = authorities;
    }

    // ===== Getter & Setter =====

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getCompany() { return company; }
    public void setCompany(int company) { this.company = company; }

    public List<String> getAuthorities() { return authorities; }
    public void setAuthorities(List<String> authorities) { this.authorities = authorities; }
}
