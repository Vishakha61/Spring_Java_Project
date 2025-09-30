package com.example.inventory.controller;

import com.example.inventory.model.Sales;
import com.example.inventory.service.BillingService;
import com.example.inventory.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;
    private final ItemService itemService;
    
    public BillingController(BillingService billingService, ItemService itemService) {
        this.billingService = billingService;
        this.itemService = itemService;
    }

    // Show billing dashboard
    @GetMapping
    public String billingDashboard(Model model) {
        model.addAttribute("title", "Billing System");
        model.addAttribute("items", itemService.getAllItems());
        return "billing/dashboard";
    }

    // Show generate bill form
    @GetMapping("/generate")
    public String showGenerateBillForm(Model model) {
        model.addAttribute("title", "Generate Bill");
        model.addAttribute("items", itemService.getAllItems());
        return "billing/generate";
    }

    // Process bill generation
    @PostMapping("/generate")
    public String generateBill(@RequestParam(value = "itemId", required = true) Long itemId, 
                               @RequestParam(value = "quantity", required = true) int quantity,
                               RedirectAttributes redirectAttributes) {
        try {
            // Validate parameters
            if (itemId == null || itemId <= 0) {
                redirectAttributes.addFlashAttribute("error", "❌ Error: Please select a valid item.");
                return "redirect:/billing/generate";
            }
            
            if (quantity <= 0) {
                redirectAttributes.addFlashAttribute("error", "❌ Error: Quantity must be greater than 0.");
                return "redirect:/billing/generate";
            }
            
            Sales sale = billingService.generateBill(itemId, quantity);
            redirectAttributes.addFlashAttribute("message", 
                "✅ Bill generated successfully! Sale ID: " + sale.getId() + 
                ", Total Amount: ₹" + String.format("%.2f", sale.getTotalAmount()));
            return "redirect:/billing/sales";
        } catch (Exception e) {
            System.err.println("Error generating bill: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "❌ Error: " + e.getMessage());
            return "redirect:/billing/generate";
        }
    }

    // Show all sales
    @GetMapping("/sales")
    public String viewSales(Model model) {
        model.addAttribute("title", "Sales History");
        List<Sales> sales = billingService.getAllSales();
        model.addAttribute("sales", sales);
        
        // Calculate total revenue
        double totalRevenue = sales.stream()
                .mapToDouble(Sales::getTotalAmount)
                .sum();
        model.addAttribute("totalRevenue", totalRevenue);
        
        return "billing/sales";
    }

    // Show sales report
    @GetMapping("/report")
    public String viewSalesReport(Model model) {
        model.addAttribute("title", "Sales Report");
        Map<String, Double> report = billingService.getSalesReportByCategory();
        model.addAttribute("reportData", report);
        
        // Calculate total revenue
        double totalRevenue = report.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        model.addAttribute("totalRevenue", totalRevenue);
        
        return "billing/report";
    }
}