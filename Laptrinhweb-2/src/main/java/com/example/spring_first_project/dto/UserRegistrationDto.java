package com.example.spring_first_project.dto;

/**
 * UserRegistrationDto
 * -------------------------------------------------------
 * DTO dùng cho việc đăng ký tài khoản thông thường
 * (không trực tiếp cho API trả JSON phức tạp).
 * Lưu thông tin người dùng mới: họ tên, email, mật khẩu,
 * công ty và chuỗi quyền hạn (authorities).
 */
public class UserRegistrationDto {

    /** Tên (first name) của người dùng */
    private String firstName;

    /** Họ (last name) của người dùng */
    private String lastName;

    /** Địa chỉ email – định danh tài khoản và đăng nhập */
    private String email;

    /** Mật khẩu (sẽ được mã hóa trước khi lưu vào DB) */
    private String password;

    /** ID công ty mà người dùng thuộc về */
    private int company;

    /**
     * Quyền hạn/role của user dạng chuỗi (ví dụ: "ROLE_USER,ROLE_ADMIN").
     * Khác với UserRegistrationApiDto (dạng List<String>).
     * Ứng dụng có thể parse chuỗi này thành danh sách khi cần.
     */
    private String authorities;

    /** Constructor đầy đủ – tiện cho việc khởi tạo nhanh */
    public UserRegistrationDto(
            String firstName,
            String lastName,
            String email,
            String password,
            int company,
            String authorities) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.company = company;
        this.authorities = authorities;
    }

    /** Constructor rỗng – cần thiết khi deserialize từ JSON/form */
    public UserRegistrationDto() {
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

    public String getAuthorities() { return authorities; }
    public void setAuthorities(String authorities) { this.authorities = authorities; }
}
