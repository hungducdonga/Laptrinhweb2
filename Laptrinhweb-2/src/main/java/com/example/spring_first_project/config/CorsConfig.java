package com.example.spring_first_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Lớp cấu hình CORS (Cross-Origin Resource Sharing)
 * Cho phép các ứng dụng front-end (React, Angular, v.v.) chạy khác domain
 * có thể gửi request đến API của Spring Boot.
 */
@Configuration // Đánh dấu đây là class cấu hình cho Spring
public class CorsConfig {

    /**
     * Bean cấu hình CORS. Spring Boot sẽ tự động quét và áp dụng.
     * @return WebMvcConfigurer để cấu hình các quy tắc CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        // Trả về 1 implementation ẩn danh của WebMvcConfigurer
        return new WebMvcConfigurer() {

            /**
             * Ghi đè phương thức addCorsMappings để định nghĩa các quy tắc CORS.
             * @param registry: nơi đăng ký các đường dẫn và thiết lập CORS
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")                 // Áp dụng cho tất cả các endpoint (/**)
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Cho phép các method này từ client
                        .allowedHeaders("*")              // Cho phép tất cả các header (Authorization, Content-Type,...)
                        .allowedOrigins("*");             // Cho phép tất cả domain gọi API (có thể giới hạn cụ thể để bảo mật hơn)
            }
        };
    }
}
