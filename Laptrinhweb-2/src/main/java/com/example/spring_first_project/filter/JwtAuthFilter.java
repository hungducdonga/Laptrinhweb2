package com.example.spring_first_project.filter;

import com.example.spring_first_project.service.JwtService;
import com.example.spring_first_project.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Đánh dấu đây là một Spring Bean, tự động được Spring quét và đăng ký
public class JwtAuthFilter extends OncePerRequestFilter {
    // Lớp filter này chạy **một lần cho mỗi request** để kiểm tra và xác thực JWT

    @Autowired
    private UserServiceImpl userDetailsService; // Service để load thông tin user từ DB

    @Autowired
    private JwtService jwtService; // Service xử lý việc tạo/giải mã/kiểm tra JWT

    /**
     * Hàm chính của filter – được Spring Security gọi cho mỗi request.
     * Nhiệm vụ:
     * 1. Lấy JWT token từ header Authorization.
     * 2. Giải mã token để lấy username.
     * 3. Kiểm tra tính hợp lệ của token.
     * 4. Nếu hợp lệ -> set thông tin xác thực vào SecurityContextHolder.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy giá trị của header Authorization từ request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Kiểm tra header có tồn tại và bắt đầu bằng "Bearer "
        // Format chuẩn: Authorization: Bearer <jwt-token>
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Cắt bỏ chuỗi "Bearer " để lấy token
            username = jwtService.extractUsername(token); // Giải mã token để lấy username
        }

        // Nếu đã có token và username và hiện tại chưa có Authentication trong SecurityContext
        if (token != null
                && username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Lấy thông tin UserDetails từ DB (hoặc nguồn khác) thông qua username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Xác thực token (chữ ký, thời gian hết hạn, v.v.)
            if (jwtService.validateToken(token, userDetails)) {
                // Tạo đối tượng Authentication cho Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,       // Principal: thông tin user
                                null,              // Credentials: không cần password
                                userDetails.getAuthorities() // Danh sách quyền (roles)
                        );

                // Gắn thông tin request hiện tại vào authToken (IP, session, …)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Lưu Authentication vào SecurityContext để Spring Security biết user đã được xác thực
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Log ra console (tùy mục đích debug)
                System.out.println("User " + authToken + " is authenticated.");
                System.out.println("User " + userDetails + " is authenticated.");
                System.out.println("User " + userDetails.getAuthorities() + " is authenticated.");
            }
        }

        // Tiếp tục truyền request/response xuống các filter hoặc controller tiếp theo
        filterChain.doFilter(request, response);
    }
}
