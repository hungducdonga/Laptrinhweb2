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

/**
 * JwtAuthFilter
 * ----------------------------
 * Filter bảo mật cho ứng dụng Spring Security, chạy **một lần cho mỗi request**.
 * Chức năng chính:
 *   - Kiểm tra header Authorization để lấy JWT token.
 *   - Giải mã token để lấy username.
 *   - Kiểm tra tính hợp lệ của token (hết hạn, chữ ký…).
 *   - Nếu hợp lệ, tạo Authentication và lưu vào SecurityContext.
 */
@Component  // Đăng ký Bean, Spring Boot tự quét và kích hoạt filter
public class JwtAuthFilter extends OncePerRequestFilter {

    // Service để load thông tin User từ DB
    @Autowired
    private UserServiceImpl userDetailsService;

    // Service xử lý việc tạo/giải mã/kiểm tra JWT
    @Autowired
    private JwtService jwtService;

    /**
     * Phương thức chính của filter, được gọi tự động cho mỗi request.
     *
     * @param request  HTTP request của client
     * @param response HTTP response trả về
     * @param filterChain chuỗi filter tiếp theo
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1️⃣ Lấy header Authorization (nếu có)
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 2️⃣ Kiểm tra header có định dạng "Bearer <token>"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Cắt bỏ tiền tố "Bearer " để lấy chuỗi JWT token
            token = authHeader.substring(7);
            // Giải mã token để lấy username
            username = jwtService.extractUsername(token);
        }

        // 3️⃣ Nếu đã lấy được token & username và chưa có Authentication trong context
        if (token != null
                && username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Lấy thông tin user từ DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 4️⃣ Kiểm tra tính hợp lệ của token (chữ ký, thời gian hết hạn,…)
            if (jwtService.validateToken(token, userDetails)) {

                // Tạo Authentication chứa thông tin user và quyền (authorities)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,  // Không cần mật khẩu
                                userDetails.getAuthorities()
                        );

                // Gắn thêm thông tin request (IP, session) vào token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 5️⃣ Lưu Authentication vào SecurityContext
                // -> Spring Security hiểu rằng user này đã được xác thực
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Log debug (tuỳ chọn)
                System.out.println("User authenticated: " + userDetails.getUsername());
                System.out.println("Authorities: " + userDetails.getAuthorities());
            }
        }

        // 6️⃣ Tiếp tục chuỗi filter/controller khác
        filterChain.doFilter(request, response);
    }
}
