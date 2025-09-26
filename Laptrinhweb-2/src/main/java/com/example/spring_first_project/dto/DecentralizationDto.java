package com.example.spring_first_project.dto;

import java.util.List;

/**
 * DecentralizationDto
 * ----------------------------
 * DTO (Data Transfer Object) dùng để truyền thông tin
 * phân quyền (decentralization/authorization) giữa client và server.
 *
 * ✔ Mục đích:
 *    - Gửi/nhận danh sách quyền (roles/authorities) của người dùng.
 *    - Giúp tách biệt lớp Entity gốc với dữ liệu truyền qua API.
 */
public class DecentralizationDto {

    /**
     * Danh sách tên quyền (Authorities/Roles) của user.
     * Ví dụ: ["ROLE_ADMIN", "ROLE_USER"]
     */
    private List<String> authorities;

    /** Constructor đầy đủ – khởi tạo với danh sách quyền */
    public DecentralizationDto(List<String> authorities) {
        this.authorities = authorities;
    }

    /** Constructor rỗng – cần thiết cho việc deserialize JSON -> Object */
    public DecentralizationDto() {
    }

    /** Lấy danh sách quyền */
    public List<String> getAuthorities() {
        return authorities;
    }

    /** Gán danh sách quyền */
    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}

