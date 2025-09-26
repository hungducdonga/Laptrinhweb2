package com.example.spring_first_project.controller;

import com.example.spring_first_project.model.UserDemo;
import com.example.spring_first_project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * MainController
 * --------------
 * Controller chính điều hướng các trang giao diện người dùng
 * (sử dụng mô hình MVC với view template như Thymeleaf).
 */
@Controller
public class MainController {

    private final UserService userService;

    /**
     * Constructor Injection:
     * Spring sẽ tự động inject một instance của UserService
     * vào MainController khi khởi tạo.
     */
    public MainController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Trang chủ
     * URL: GET /
     * Trả về view "index" (ví dụ: index.html trong thư mục templates).
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Trang danh sách người dùng
     * URL: GET /user
     * - Lấy tất cả người dùng từ database thông qua userService
     * - Gửi danh sách đó sang view "user" để hiển thị.
     */
    @GetMapping("/user")
    public String trangChiTiet(Model model) {
        // Lấy toàn bộ danh sách người dùng từ service
        List<UserDemo> list = userService.findAll();

        // Thêm dữ liệu vào model để Thymeleaf/JSP có thể render
        // Trong view, truy cập với tên attribute "user"
        model.addAttribute("user", list);

        // Trả về tên view hiển thị (user.html)
        return "user";
    }
}
