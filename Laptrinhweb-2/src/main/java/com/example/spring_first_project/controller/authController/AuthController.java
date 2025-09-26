package com.example.spring_first_project.controller.authController;

import com.example.spring_first_project.dto.ApiResponse;
import com.example.spring_first_project.dto.LoginResponse;
import com.example.spring_first_project.dto.UserLoginDto;
import com.example.spring_first_project.dto.UserRegistrationApiDto;
import com.example.spring_first_project.model.UserDemo;
import com.example.spring_first_project.service.JwtService;
import com.example.spring_first_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController
 * --------------
 * Quản lý API xác thực và đăng ký người dùng.
 * Chức năng:
 *   - Đăng ký tài khoản mới
 *   - Đăng nhập & sinh JWT
 *   - Sinh token (generateToken) cho client
 */
@RestController
@RequestMapping("/api") // Mọi endpoint trong class này đều bắt đầu với "/api"
public class AuthController {

    @Autowired
    private UserService userService; // Xử lý logic liên quan đến User (DB, tạo user, lấy thông tin user…)

    @Autowired
    private AuthenticationManager authenticationManager; // Dùng Spring Security để xác thực email & password

    @Autowired
    private JwtService jwtService; // Tạo và kiểm tra tính hợp lệ của JWT

    /**
     * API: Đăng ký người dùng mới
     * URL: POST /api/auth/register
     */
    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<UserDemo>> createUser(@RequestBody UserRegistrationApiDto userRegistrationApiDto) {
        try {
            // Gọi service để lưu người dùng mới vào DB
            UserDemo newUser = userService.saveUserWithApi(userRegistrationApiDto);
            // Trả về JSON thành công
            return ResponseEntity.ok(new ApiResponse<>(200, "User just creation", newUser));
        } catch (UsernameNotFoundException e) {
            // Trường hợp không tìm thấy username hoặc vi phạm quyền
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        } catch (Exception e) {
            // Các lỗi khác: DB, runtime…
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }

    /**
     * API: Đăng nhập để lấy JWT token
     * URL: POST /api/auth/login
     * Input: email & password
     */
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody UserLoginDto userLoginDto) {
        try {
            // Xác thực tài khoản bằng AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDto.getEmail(),
                            userLoginDto.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                // Lấy thông tin người dùng (UserDetails) từ DB
                UserDetails userDetails = userService.loadUserByUsername(userLoginDto.getEmail());
                // Lấy danh sách quyền (ví dụ: ROLE_USER, ROLE_ADMIN)
                for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
                    System.out.println(grantedAuthority.getAuthority()); // Log quyền ra console
                    // Tạo JWT token và trả về cho client
                    return ResponseEntity.ok(new ApiResponse<>(
                            200,
                            "Generated token successful",
                            userService.login(
                                    userLoginDto,
                                    jwtService.generateToken(
                                            userLoginDto.getEmail(),
                                            grantedAuthority.getAuthority()
                                    )
                            )
                    ));
                }
            }
            // Nếu xác thực thất bại
            throw new BadCredentialsException("Bad credentials");
        } catch (BadCredentialsException e) {
            // Sai email hoặc password
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied because invalid username", null));
        } catch (Exception e) {
            // Lỗi hệ thống
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }

    /**
     * API: Sinh token sau khi xác thực (tương tự login)
     * URL: POST /api/auth/generateToken
     * Input: email & password
     */
    @PostMapping("/auth/generateToken")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticateAndGetToken(@RequestBody UserLoginDto authRequest) {
        try {
            // Xác thực người dùng
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
                for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
                    System.out.println(grantedAuthority.getAuthority());
                    // Tạo và trả về JWT token
                    return ResponseEntity.ok(new ApiResponse<>(
                            200,
                            "Generated token successful",
                            userService.login(
                                    authRequest,
                                    jwtService.generateToken(
                                            authRequest.getEmail(),
                                            grantedAuthority.getAuthority()
                                    )
                            )
                    ));
                }
            }
            throw new BadCredentialsException("Bad credentials");
        } catch (BadCredentialsException e) {
            // Đăng nhập sai thông tin
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        }
    }
}
