package com.example.spring_first_project.controller;

import com.example.spring_first_project.model.Role;
import com.example.spring_first_project.service.RoleServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller()
public class RoleController {

    private RoleServiceImpl roleService;

    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/role")
    public String role(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "role";
    }

    @GetMapping("/addRole")
    public String addRole(Model model) {
        model.addAttribute("role", new Role());
        return "addRole";
    }

    @PostMapping("addRole")
    public String addRole(@ModelAttribute("role") Role role, RedirectAttributes redirectAttributes) {
        roleService.saveOrUpdate(role);
        return "redirect:/role";
    }
}
