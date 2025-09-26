package com.example.spring_first_project.dto;

/**
 * UserLoginDto
 * --------------------------------
 * DTO dùng để nhận dữ liệu đăng nhập từ client (React, Angular, ...).
 * Chỉ chứa email và password, đủ để xác thực user.
 */
public class UserLoginDto {

    /** Email của người dùng (dùng để đăng nhập) */
    private String email;

    /** Mật khẩu người dùng (sẽ được so sánh với mật khẩu đã mã hoá trong DB) */
    private String password;

    /** Constructor đầy đủ (dùng khi muốn khởi tạo nhanh đối tượng) */
    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /** Constructor rỗng – cần thiết cho quá trình deserialize JSON */
    public UserLoginDto() {
    }

    // ===== Getter & Setter =====

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

