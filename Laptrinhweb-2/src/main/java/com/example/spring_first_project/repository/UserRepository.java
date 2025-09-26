package com.example.spring_first_project.repository;

import com.example.spring_first_project.model.UserDemo;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository cho Entity UserDemo
 * - Quản lý các thao tác CRUD với bảng user_demo (tên bảng mặc định lấy từ Entity).
 * - Sử dụng Spring Data JPA nên không cần viết SQL thủ công.
 */
@Repository
public interface UserRepository extends CrudRepository<UserDemo, Integer> {

    /**
     * Lấy toàn bộ danh sách người dùng.
     * - Trả về List<UserDemo> thay vì Iterable.
     * - @NotNull: đảm bảo giá trị trả về không phải null.
     * - Spring Data JPA tự động tạo câu SQL: SELECT * FROM user_demo;
     */
    @NotNull
    List<UserDemo> findAll();

    /**
     * Tìm thông tin người dùng theo email.
     * - Tự động sinh câu SQL: SELECT * FROM user_demo WHERE email = ?;
     *
     * @param email địa chỉ email cần tìm
     * @return đối tượng UserDemo nếu tìm thấy, nếu không trả về null.
     */
    UserDemo findByEmail(String email);
}
