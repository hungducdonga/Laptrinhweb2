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
 * - Quản lý các API liên quan đến xác thực và đăng ký người dùng
 */
@RestController
@RequestMapping("/api") // Prefix chung cho tất cả endpoint trong controller này
public class AuthController {

    @Autowired
    private UserService userService;                 // Service xử lý logic liên quan tới User

    @Autowired
    private AuthenticationManager authenticationManager; // Quản lý xác thực Spring Security

    @Autowired
    private JwtService jwtService;                   // Service tạo và xác thực JWT token

    /**
     * API Đăng ký người dùng mới
     * URL: POST /api/auth/register
     */
    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<UserDemo>> createUser(@RequestBody UserRegistrationApiDto userRegistrationApiDto) {
        try {
            // Gọi service để lưu thông tin người dùng mới
            UserDemo newUser = userService.saveUserWithApi(userRegistrationApiDto);
            // Trả về phản hồi thành công
            return ResponseEntity.ok(new ApiResponse<>(200, "User just creation", newUser));
        } catch (UsernameNotFoundException e) {
            // Trường hợp không tìm thấy username hoặc quyền truy cập bị từ chối
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        } catch (Exception e) {
            // Lỗi hệ thống khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }

    /**
     * API Đăng nhập để lấy JWT token
     * URL: POST /api/auth/login
     */
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody UserLoginDto userLoginDto) {
        try {
            // Xác thực thông tin email & password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDto.getEmail(),
                            userLoginDto.getPassword()
                    )
            );

            // Nếu xác thực thành công
            if (authentication.isAuthenticated()) {
                // Lấy thông tin người dùng từ DB
                UserDetails userDetails = userService.loadUserByUsername(userLoginDto.getEmail());
                // Lấy danh sách quyền (ROLE_USER, ROLE_ADMIN,...)
                for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
                    System.out.println(grantedAuthority.getAuthority()); // Log ra console quyền của user
                    // Sinh JWT token và trả về cho client
                    return ResponseEntity.ok(new ApiResponse<>(
                            200,
                            "Generated token successful",
                            userService.login(
                                    userLoginDto,
                                    jwtService.generateToken(userLoginDto.getEmail(), grantedAuthority.getAuthority())
                            )));
                }
            }
            // Nếu thông tin sai -> ném exception
            throw new BadCredentialsException("Bad credentials");
        } catch (BadCredentialsException e) {
            // Trả về lỗi 403 khi username hoặc password sai
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied because invalid username", null));
        } catch (Exception e) {
            // Lỗi khác (server, DB,…)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An error occurred", null));
        }
    }

    /**
     * API Sinh token sau khi xác thực (tương tự login, có thể tách riêng khi cần)
     * URL: POST /api/auth/generateToken
     */
    @PostMapping("/auth/generateToken")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticateAndGetToken(@RequestBody UserLoginDto authRequest) {
        try {
            // Xác thực email & password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                // Lấy thông tin người dùng từ DB
                UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
                for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
                    System.out.println(grantedAuthority.getAuthority());
                    // Sinh token và trả về
                    return ResponseEntity.ok(new ApiResponse<>(
                            200,
                            "Generated token successful",
                            userService.login(
                                    authRequest,
                                    jwtService.generateToken(authRequest.getEmail(), grantedAuthority.getAuthority())
                            )));
                }
            }
            // Nếu thông tin sai
            throw new BadCredentialsException("Bad credentials");
        } catch (BadCredentialsException e) {
            // Sai thông tin đăng nhập
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access Denied", null));
        }
    }
}
