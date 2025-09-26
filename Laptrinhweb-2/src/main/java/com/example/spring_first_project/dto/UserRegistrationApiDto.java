package com.example.spring_first_project.dto;

import java.util.List;

/**
 * UserRegistrationApiDto
 * -------------------------------------------------------
 * DTO dùng cho API đăng ký tài khoản (User Registration).
 * Nhận toàn bộ thông tin cần thiết từ client để tạo mới user,
 * đồng thời gán công ty và phân quyền (authorities) ban đầu.
 */
public class UserRegistrationApiDto {

    /** Tên (first name) của người dùng */
    private String firstName;

    /** Họ (last name) của người dùng */
    private String lastName;

    /** Địa chỉ email – dùng để đăng nhập và định danh tài khoản */
    private String email;

    /** Mật khẩu – sẽ được mã hóa trước khi lưu DB */
    private String password;

    /** ID công ty mà user thuộc về (liên kết tới bảng Company) */
    private int company;

    /** Danh sách quyền/roles mà user được cấp khi tạo tài khoản */
    private List<String> authorities;

    /** Constructor rỗng – cần thiết cho quá trình JSON deserialization */
    public UserRegistrationApiDto() {}

    /**
     * Constructor đầy đủ – tiện cho việc tạo đối tượng thủ công.
     */
    public UserRegistrationApiDto(
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
