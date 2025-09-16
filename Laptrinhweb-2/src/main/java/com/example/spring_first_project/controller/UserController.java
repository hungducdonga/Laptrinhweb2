package com.example.spring_first_project.controller;

import com.example.spring_first_project.dto.UserRegistrationDto;
import com.example.spring_first_project.model.UserDemo;
import com.example.spring_first_project.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.logging.Logger;

@Controller()
@RequestMapping("/")
public class UserController {

    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping("/addUser")
    public String addCompany() {
        return "addUser";
    }

    @PostMapping("/addUser")
    public String addCompany(@ModelAttribute("user") UserRegistrationDto userRegistrationDto, RedirectAttributes redirectAttributes) {
        userService.save(userRegistrationDto);
        return "redirect:/user";
    }

    @GetMapping("/loginUser")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserDemo());
        return "login";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id) {
//        userService.deleteUserById(id);
        return "redirect:/user";
    }
}