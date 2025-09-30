package com.example.inventory.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            if (statusCode == 404) {
                model.addAttribute("error", "Page Not Found");
                model.addAttribute("message", "The page you're looking for doesn't exist.");
            } else if (statusCode == 500) {
                model.addAttribute("error", "Internal Server Error");
                model.addAttribute("message", "Something went wrong on our end. Please try again later.");
            } else {
                model.addAttribute("error", "Error " + statusCode);
                model.addAttribute("message", "An unexpected error occurred.");
            }
        } else {
            model.addAttribute("error", "Unknown Error");
            model.addAttribute("message", "An unexpected error occurred.");
        }
        
        return "error";
    }
}