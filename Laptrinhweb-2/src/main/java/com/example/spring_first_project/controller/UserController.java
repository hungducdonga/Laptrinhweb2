package com.example.spring_first_project.controller;

import com.example.spring_first_project.dto.UserRegistrationDto;
import com.example.spring_first_project.model.UserDemo;
import com.example.spring_first_project.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/") // Tất cả endpoint trong controller này sẽ bắt đầu từ "/"
public class UserController {

    // Service xử lý các nghiệp vụ liên quan đến User
    private final UserServiceImpl userService;

    // Constructor injection để Spring tự inject UserServiceImpl
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * @ModelAttribute("user")
     * - Tạo trước một đối tượng UserRegistrationDto rỗng
     * - Được gọi trước mỗi handler method và gắn vào Model với key "user"
     * - Dùng để binding form thêm user.
     */
    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    /**
     * Hiển thị form thêm người dùng mới
     * URL: GET /addUser
     * - Trả về template addUser.html
     */
    @GetMapping("/addUser")
    public String addCompany() {
        return "addUser";
    }

    /**
     * Xử lý submit form thêm người dùng
     * URL: POST /addUser
     * - Nhận dữ liệu từ form dưới dạng UserRegistrationDto
     * - Gọi service để lưu thông tin vào DB
     * - Redirect về trang danh sách user (/user)
     */
    @PostMapping("/addUser")
    public String addCompany(@ModelAttribute("user") UserRegistrationDto userRegistrationDto,
                             RedirectAttributes redirectAttributes) {
        userService.save(userRegistrationDto);
        // Có thể thêm thông báo flash:
        // redirectAttributes.addFlashAttribute("message", "Thêm user thành công");
        return "redirect:/user";
    }

    /**
     * Hiển thị form đăng nhập
     * URL: GET /loginUser
     * - Gắn đối tượng UserDemo rỗng vào model để binding dữ liệu form.
     * - Trả về view login.html
     */
    @GetMapping("/loginUser")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserDemo());
        return "login";
    }

    /**
     * Xóa người dùng theo id
     * URL: GET /deleteUser/{id}
     * - Gọi service xóa user theo id
     * - Sau đó redirect về danh sách user
     */
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        // Đang bị comment: cần mở để xóa thực tế
        // userService.deleteUserById(id);
        return "redirect:/user";
    }
}
