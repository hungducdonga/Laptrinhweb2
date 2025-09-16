package com.example.spring_first_project.config;

import com.example.spring_first_project.filter.JwtAuthFilter;
import com.example.spring_first_project.service.UserService;
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
 * Cấu hình bảo mật Spring Security cho ứng dụng
 */
@Configuration                  // Đánh dấu đây là class cấu hình Spring
@EnableWebSecurity             // Bật tính năng bảo mật của Spring Security
public class SecurityConfig {

    // Service cung cấp thông tin user cho quá trình xác thực
    private final UserServiceImpl userService;
    // Filter tự định nghĩa để kiểm tra JWT token
    private final JwtAuthFilter authFilter;

    // @Lazy để tránh vòng lặp phụ thuộc khi khởi tạo Bean
    public SecurityConfig(@Lazy UserServiceImpl userService, @Lazy JwtAuthFilter authFilter) {
        this.userService = userService;
        this.authFilter = authFilter;
    }

    /**
     * Bean mã hóa mật khẩu với thuật toán BCrypt
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cấu hình Provider để Spring Security biết cách xác thực người dùng
     * sử dụng UserServiceImpl và BCrypt để kiểm tra mật khẩu
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);      // Lấy thông tin user từ DB
        authProvider.setPasswordEncoder(passwordEncoder());   // So khớp mật khẩu đã mã hóa
        return authProvider;
    }

    /**
     * Cấu hình các rule bảo mật cho HTTP request
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers(httpSecurityHeadersConfigurer -> {
                    // Cho phép hiển thị H2 Console (vô hiệu hóa frameOptions)
                    httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                })
                .csrf().disable() // Tắt CSRF vì sử dụng JWT (stateless)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // Các endpoint cho phép truy cập không cần đăng nhập
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
                        // Chỉ ROLE_ADMIN mới được truy cập /api/users
                        .requestMatchers("/api/users").hasAnyAuthority("ROLE_ADMIN")
                        // ROLE_USER hoặc ROLE_ADMIN được phép vào /api/companies
                        .requestMatchers("/api/companies").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        // Các request còn lại phải đăng nhập mới truy cập được
                        .anyRequest().authenticated()
                )
                // Cấu hình logout: sau khi logout chuyển hướng về /login
                .logout(logout -> logout
                        .logoutSuccessUrl("/login").permitAll()
                )
                // Sử dụng JWT nên không tạo session cho người dùng
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Thêm bộ lọc JWT trước khi chạy UsernamePasswordAuthenticationFilter
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Cung cấp AuthenticationManager cho quá trình xác thực
     */
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
