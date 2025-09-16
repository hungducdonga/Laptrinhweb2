package com.example.spring_first_project.controller.restController;

import com.example.spring_first_project.dto.*;
import com.example.spring_first_project.model.Company;
import com.example.spring_first_project.model.Role;
import com.example.spring_first_project.model.UserDemo;
import com.example.spring_first_project.repository.CompanyRepository;
import com.example.spring_first_project.repository.UserRepository;
import com.example.spring_first_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController // Đánh dấu class là RestController, trả dữ liệu JSON/XML cho client
@RequestMapping("/api") // Định nghĩa prefix URL cho toàn bộ API của class này
public class RestUserController {

    @Autowired
    private UserRepository userRepository; // Repository làm việc trực tiếp với bảng User trong DB

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; // Dùng để mã hoá mật khẩu

    @Autowired
    private CompanyRepository companyRepository; // Repository để lấy thông tin công ty của user

    private UserService userService; // Service xử lý business logic liên quan đến User

    // Constructor Injection cho UserService
    public RestUserController(UserService userService) {
        this.userService = userService;
    }

    // Lấy danh sách toàn bộ User
    @GetMapping("/users")
    public ResponseEntity<List<UserApiResponse>> getUsers() {
        List<UserDemo> users = userService.findAll(); // Lấy tất cả User từ DB
        List<UserApiResponse> userResponses = new ArrayList<>(); // Danh sách DTO trả về client
        UserApiResponse userApiResponse = null;

        for (UserDemo user : users) {
            if (user.getCompany() != null) { // Nếu user có công ty
                Company company = companyRepository.findById(user.getCompany().getId()).orElse(null);
                for (Role role : user.getAuthorities()) { // Lấy danh sách role của user
                    Role rol = new Role(role.getAuthority());
                    userApiResponse = new UserApiResponse(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            bCryptPasswordEncoder.encode(user.getPassword()), // Mật khẩu được mã hoá
                            company,
                            List.of(rol)
                    );
                }
                userResponses.add(userApiResponse);
            } else { // Nếu user không có công ty
                for (Role role : user.getAuthorities()) {
                    Role rol = new Role(role.getAuthority());
                    userApiResponse = new UserApiResponse(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            bCryptPasswordEncoder.encode(user.getPassword()),
                            List.of(rol)
                    );
                }
                userResponses.add(userApiResponse);
            }
        }
        return ResponseEntity.ok(userResponses); // Trả về danh sách User kèm roles và company
    }

    // Tìm user theo email (string)
    @GetMapping("/users/string/{email}")
    public ResponseEntity<ApiResponse<UserDemo>> getUser(@PathVariable String email) {
        try {
            UserDemo user = userService.findByUsername(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "User not found", null));
            }
            return ResponseEntity.ok(new ApiResponse<>(200, "User found", user));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }

    // Tìm user theo ID (int)
    @GetMapping("/users/int/{id}")
    public ResponseEntity<ApiResponse<UserDemo>> findUserById(@PathVariable int id) {
        try {
            UserDemo user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "User not found", null));
            }
            return ResponseEntity.ok(new ApiResponse<>(200, "User found", user));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }

    // Cập nhật thông tin user
    @PutMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserDemo>> updateUser(@PathVariable int id, @RequestBody UserUpdateApiDto userUpdateApiDto) {
        try {
            UserDemo updateUser = userService.updateUserWithApi(userUpdateApiDto, id);
            return ResponseEntity.ok(new ApiResponse<>(200, "User just update", updateUser));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }

    // Phân quyền cho user
    @PutMapping("/decentralization/{id}")
    public ResponseEntity<ApiResponse<UserDemo>> decentralizationUser(@PathVariable int id, @RequestBody DecentralizationDto decentralizationDto) {
        try {
            UserDemo updateUser = userService.decentralizationWithApi(decentralizationDto, id);
            return ResponseEntity.ok(new ApiResponse<>(200, "User just decentralization", updateUser));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }

    // Xoá user theo ID
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUserWithApi(id);
    }

    // Lấy danh sách roles của user theo ID
    @GetMapping("/{userId}/roles")
    public Object getUserRoles(@PathVariable int userId) {
        Collection<Role> roles = userService.getUserRoles(userId);
        System.out.println(roles);
        if (roles == null) {
            return ResponseEntity.notFound().build();
        }
        return roles;
    }
}
