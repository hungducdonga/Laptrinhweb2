package com.example.spring_first_project.repository;

import com.example.spring_first_project.model.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository cho Entity Role
 * - Kế thừa CrudRepository để sử dụng các hàm CRUD mặc định.
 * - Được Spring Data JPA tự động sinh truy vấn, không cần viết SQL thủ công.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    /**
     * Lấy toàn bộ danh sách Role dưới dạng List.
     * - @NotNull đảm bảo kết quả trả về không null.
     * - Spring Data JPA sẽ tự động tạo câu lệnh SELECT * FROM role.
     */
    @NotNull
    List<Role> findAll();

    /**
     * Tìm Role theo tên quyền (authority).
     * - Ví dụ: SELECT * FROM role WHERE authority = ?;
     * - Spring Data tự phân tích tên hàm "findByAuthority" để tạo truy vấn.
     *
     * @param authority tên quyền (VD: "ROLE_USER", "ROLE_ADMIN")
     * @return Role tương ứng nếu tìm thấy, nếu không trả về null.
     */
    Role findByAuthority(String authority);
}

