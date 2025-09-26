package com.example.spring_first_project.dto;

/**
 * LoginResponse
 * ----------------------------
 * DTO (Data Transfer Object) trả về cho client sau khi đăng nhập thành công.
 *
 * ✔ Mục đích:
 *    - Chứa thông tin người dùng và token JWT để client sử dụng
 *      cho các request tiếp theo.
 */
public class LoginResponse {

    /**
     * Thông tin đăng nhập của user (username, v.v.).
     * Được gói trong một DTO khác: UserLoginDto.
     */
    private UserLoginDto userLoginDto;

    /**
     * Quyền (role) chính của user, ví dụ: "ROLE_ADMIN" hoặc "ROLE_USER".
     * Giúp frontend kiểm soát giao diện theo quyền.
     */
    private String role;

    /**
     * Chuỗi JWT token mà backend tạo ra khi đăng nhập thành công.
     * Client lưu token này (thường trong localStorage/cookie) để
     * gửi kèm theo mỗi request cần xác thực.
     */
    private String token;

    /**
     * Địa chỉ email của người dùng.
     * Thuận tiện cho việc hiển thị hoặc xác thực phía client.
     */
    private String email;

    /** Constructor đầy đủ – gán tất cả các thuộc tính */
    public LoginResponse(UserLoginDto userLoginDto, String token, String role, String email) {
        this.userLoginDto = userLoginDto;
        this.token = token;
        this.role = role;
        this.email = email;
    }

    /** Constructor rỗng – cần thiết cho quá trình deserialize JSON */
    public LoginResponse() {
    }

    // ===== Getter & Setter =====

    public UserLoginDto getUserLoginDto() {
        return userLoginDto;
    }

    public void setUserLoginDto(UserLoginDto userLoginDto) {
        this.userLoginDto = userLoginDto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

