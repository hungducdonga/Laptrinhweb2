package com.example.spring_first_project.config;

import com.example.spring_first_project.filter.JwtAuthFilter;
import com.example.spring_first_project.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig
 * ---------------
 * Cấu hình Spring Security:
 *  - Xác thực người dùng bằng JWT.
 *  - Phân quyền truy cập các endpoint.
 *  - Mã hóa mật khẩu với BCrypt.
 */
@Configuration                 // Đánh dấu đây là class cấu hình của Spring
@EnableWebSecurity            // Kích hoạt Spring Security
public class SecurityConfig {

    /** Service lấy thông tin người dùng từ DB để xác thực */
    private final UserServiceImpl userService;

    /** Bộ lọc tự định nghĩa: đọc & xác minh JWT trong mỗi request */
    private final JwtAuthFilter authFilter;

    /**
     * Constructor
     * @Lazy: tránh lỗi vòng lặp phụ thuộc khi Spring khởi tạo Bean
     */
    public SecurityConfig(@Lazy UserServiceImpl userService,
                          @Lazy JwtAuthFilter authFilter) {
        this.userService = userService;
        this.authFilter = authFilter;
    }

    /** 
     * Bean mã hóa mật khẩu 
     * Sử dụng thuật toán BCrypt để lưu và so khớp mật khẩu an toàn
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication Provider
     *  - Gắn UserServiceImpl để truy xuất người dùng.
     *  - Gắn BCryptPasswordEncoder để so sánh mật khẩu.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Cấu hình các quy tắc bảo mật HTTP
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers(h -> 
                // Cho phép hiển thị H2-console (vô hiệu hóa frameOptions)
                h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        )
        .csrf().disable() // Tắt CSRF vì JWT không dùng session
        .authorizeHttpRequests(auth -> auth
            // Những đường dẫn cho phép truy cập không cần đăng nhập
            .requestMatchers(
                "/registration/**",
                "api/company/**",
                "api/roles",
                "api/user/**",
                "api/decentralization/**",
                "api/users/**",
                "/api/companies",
                "/api/company/**",
                "/api/auth/**",
                "/api/auth/login",
                "/api/auth/generateToken",
                "/h2-console/**"
            ).permitAll()

            // Chỉ người có ROLE_ADMIN mới truy cập được /api/users
            .requestMatchers("/api/users").hasAnyAuthority("ROLE_ADMIN")

            // Người có ROLE_USER hoặc ROLE_ADMIN được phép vào /api/companies
            .requestMatchers("/api/companies")
            .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

            // Mọi request khác phải đăng nhập mới truy cập được
            .anyRequest().authenticated()
        )
        // Cấu hình logout: sau khi logout chuyển hướng về /login
        .logout(logout -> logout.logoutSuccessUrl("/login").permitAll())

        // JWT nên để trạng thái stateless: không tạo session server-side
        .sessionManagement(s -> s
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        // Thêm bộ lọc JWT trước bộ lọc xác thực Username/Password mặc định
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Cung cấp AuthenticationManager
     * Dùng cho quá trình xác thực đăng nhập.
     */
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
