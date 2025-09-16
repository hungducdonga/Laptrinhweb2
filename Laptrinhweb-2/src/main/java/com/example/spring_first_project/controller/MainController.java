package com.example.spring_first_project.controller;

import com.example.spring_first_project.model.UserDemo;
import com.example.spring_first_project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String trangChiTiet(Model model) {
        List<UserDemo> list = userService.findAll();
        model.addAttribute("user", list);
        return "user";
    }
}
