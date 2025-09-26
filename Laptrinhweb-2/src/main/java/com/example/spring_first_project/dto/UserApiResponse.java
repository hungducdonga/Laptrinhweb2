package com.example.spring_first_project.dto;

import com.example.spring_first_project.model.Company;
import com.example.spring_first_project.model.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Collection;

/**
 * UserApiResponse
 * ---------------------------------
 * DTO trả về cho client khi cần hiển thị hoặc gửi dữ liệu User qua API.
 * 
 * ❗ Mặc dù chứa annotation JPA, lớp này KHÔNG nên là entity chính,
 *     mà chỉ là Data Transfer Object (DTO) để:
 *       - Chuyển dữ liệu User ra ngoài (REST API).
 *       - Giúp tránh lộ các chi tiết nhạy cảm không cần thiết.
 */
public class UserApiResponse {

    /** ID định danh của user */
    private Integer id;

    /** Tên */
    @Column()
    private String firstName;

    /** Họ */
    @Column()
    private String lastName;

    /** Email duy nhất của user (không được null) */
    @Column(unique = true, nullable = false)
    private String email;

    /** Mật khẩu (thường đã được mã hoá khi lưu DB) */
    @Column(nullable = false)
    private String password;

    /**
     * Công ty mà user thuộc về.
     * @ManyToOne: nhiều user thuộc một company.
     * @JsonBackReference: tránh vòng lặp JSON khi serialize
     * (Company chứa List<User> -> User chứa Company).
     */
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true, referencedColumnName = "company_id")
    @JsonBackReference
    private Company company;

    /**
     * Danh sách các quyền (roles) của user.
     * @ManyToMany: một user có thể có nhiều role, ngược lại.
     * FetchType.EAGER: luôn lấy roles cùng lúc khi truy vấn user.
     * Bảng liên kết: user_role_function
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_function",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Collection<Role> authorities;

    // ===== Constructor =====

    /** Constructor đầy đủ tất cả thuộc tính */
    public UserApiResponse(Integer id, String firstName, String lastName,
                           String email, String password,
                           Company company, Collection<Role> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.company = company;
        this.authorities = authorities;
    }

    /** Constructor không kèm company */
    public UserApiResponse(Integer id, String firstName, String lastName,
                           String email, String password,
                           Collection<Role> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /** Constructor không id (dùng khi tạo mới) */
    public UserApiResponse(String firstName, String lastName,
                           String email, String password,
                           Company company, Collection<Role> authorities) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.company = company;
        this.authorities = authorities;
    }

    /** Constructor chỉ có email, password, company, authorities */
    public UserApiResponse(String email, String password,
                           Company company, Collection<Role> authorities) {
        this.email = email;
        this.password = password;
        this.company = company;
        this.authorities = authorities;
    }

    /** Constructor rỗng – cần thiết cho quá trình JSON deserialize */
    public UserApiResponse() {}

    // ===== Getter & Setter =====

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public Collection<Role> getAuthorities() { return authorities; }
    public void setAuthorities(Collection<Role> authorities) { this.authorities = authorities; }
}

