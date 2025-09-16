package com.example.spring_first_project.controller.restController;

import com.example.spring_first_project.dto.UserRegistrationDto;
import com.example.spring_first_project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // Đánh dấu đây là Spring MVC Controller (trả về tên view chứ không trả JSON như @RestController)
@RequestMapping("/registration") // Định nghĩa URL gốc cho controller này: /registration
public class UserRegistrationController {

    private UserService userService; // Service xử lý nghiệp vụ liên quan đến User

    // Constructor Injection: Spring sẽ tự inject UserService khi tạo controller
    public UserRegistrationController(UserService userService) {
        super(); // Gọi constructor của lớp cha (Object), không bắt buộc
        this.userService = userService;
    }

    /**
     * Phương thức này sẽ được gọi trước mỗi request handler trong controller
     * để gắn một đối tượng UserRegistrationDto trống vào model,
     * key của object này trong model là "user".
     * => View (registration.html) có thể binding form với thuộc tính "user"
     */
    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    /**
     * Xử lý GET request tới /registration
     * Trả về tên view "registration" (ví dụ: resources/templates/registration.html nếu dùng Thymeleaf)
     */
    @GetMapping
    public String registrationForm() {
        return "registration"; // Render trang đăng ký
    }

    /**
     * Xử lý POST request khi người dùng submit form đăng ký.
     * @ModelAttribute("user") tự động lấy dữ liệu form và map vào UserRegistrationDto.
     * Sau khi lưu user thành công, redirect về /registration kèm query ?success để báo thành công.
     */
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto userRegistrationDto) {
        userService.save(userRegistrationDto); // Gọi service để lưu thông tin user vào DB
        return "redirect:/registration?success"; // Redirect và thêm query param để hiển thị thông báo
    }
}
