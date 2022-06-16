package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService,
                           UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String allUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "admin";
    }

    @GetMapping()
    public String showAllUsers(Model model, @AuthenticationPrincipal User user) {
        List<User> users = userService.findAll();
        Set<Role> listRoles = userService.getAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("userObj", new User());
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("userRep", userRepository.findByUsername(user.getUsername()));
        return "admin";
    }

    @PostMapping("/create")
    public String createUser(User user, @RequestParam("listRoles") Set<Role> roles) {
        userService.saveUser(user, roles);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/update/{id}")
    public String updateUserForm(@ModelAttribute("user") User user, @RequestParam("listRoles") Set<Role> roles) {
        userService.saveUser(user, roles);
        return "redirect:/admin";
    }
}

