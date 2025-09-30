package com.example.inventory.controller;

import com.example.inventory.model.Item;
import com.example.inventory.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public String viewInventory(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "inventory/list";
    }

    @GetMapping("/add")
    public String showAddItemForm(Model model) {
        model.addAttribute("item", new Item());
        return "inventory/add";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item) {
        itemService.saveItem(item);
        return "redirect:/inventory";
    }

    @GetMapping("/edit/{id}")
    public String showEditItemForm(@PathVariable Long id, Model model) {
        Item item = itemService.getItemById(id);
        model.addAttribute("item", item);
        return "inventory/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateItem(@PathVariable Long id, @ModelAttribute Item item) {
        item.setId(id);
        itemService.saveItem(item);
        return "redirect:/inventory";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return "redirect:/inventory";
    }
}