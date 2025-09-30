package com.example.inventory.controller;

import com.example.inventory.dto.LoginRequest;
import com.example.inventory.dto.SignupRequest;
import com.example.inventory.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, 
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully!");
        }
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("signupRequest") SignupRequest signupRequest,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match!");
            return "auth/register";
        }

        if (userService.existsByUsername(signupRequest.getUsername())) {
            model.addAttribute("error", "Username is already taken!");
            return "auth/register";
        }

        if (userService.existsByEmail(signupRequest.getEmail())) {
            model.addAttribute("error", "Email is already in use!");
            return "auth/register";
        }

        try {
            userService.registerUser(signupRequest);
            redirectAttributes.addFlashAttribute("message", "User registered successfully!");
            return "redirect:/login";
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error occurred while registering user: " + e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}