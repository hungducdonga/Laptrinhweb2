package com.example.spring_first_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Lớp cấu hình CORS (Cross-Origin Resource Sharing)
 * -------------------------------------------------
 * Mục đích:
 *  - Cho phép các ứng dụng front-end (React, Angular, Vue, v.v.) 
 *    chạy trên domain/port khác có thể gọi API của Spring Boot.
 *  - Tránh lỗi CORS (Cross-Origin Request Blocked) khi phát triển 
 *    frontend và backend tách biệt.
 */
@Configuration // Đánh dấu đây là lớp cấu hình cho Spring Boot
public class CorsConfig {

    /**
     * Bean cấu hình CORS.
     * Khi Spring Boot khởi động, nó sẽ tìm và áp dụng Bean này.
     * @return WebMvcConfigurer để thiết lập quy tắc CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        // Trả về một implementation ẩn danh của WebMvcConfigurer
        return new WebMvcConfigurer() {

            /**
             * Ghi đè phương thức addCorsMappings để định nghĩa các quy tắc CORS.
             * @param registry nơi đăng ký các mapping và thiết lập CORS
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép tất cả endpoint (VD: /api/**)
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Các phương thức HTTP cho phép
                        .allowedHeaders("*")  // Cho phép tất cả header (Authorization, Content-Type, v.v.)
                        .allowedOrigins("*"); // Cho phép mọi domain (http://localhost:3000, …) gọi API
                // ⚠️ Trong môi trường production, nên giới hạn cụ thể domain:
                // .allowedOrigins("https://your-frontend.com")
            }
        };
    }
}
