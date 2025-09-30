package com.example.inventory.config;

import com.example.inventory.model.Item;
import com.example.inventory.service.ItemService;
import com.example.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ItemService itemService;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles in the database
        userService.initializeRoles();
        System.out.println("Default roles initialized successfully!");
        
        // Add some sample items if none exist
        if (itemService.getAllItems().isEmpty()) {
            initializeSampleItems();
            System.out.println("Sample items added successfully!");
        }
    }
    
    private void initializeSampleItems() {
        // Sample Electronics items
        Item laptop = new Item();
        laptop.setName("Dell Laptop");
        laptop.setCategory("Electronics");
        laptop.setPrice(74699.00);
        laptop.setQuantity(15);
        laptop.setDescription("High-performance laptop for business and personal use");
        itemService.saveItem(laptop);
        
        Item smartphone = new Item();
        smartphone.setName("Samsung Galaxy");
        smartphone.setCategory("Electronics");
        smartphone.setPrice(58099.00);
        smartphone.setQuantity(25);
        smartphone.setDescription("Latest Android smartphone with advanced features");
        itemService.saveItem(smartphone);
        
        // Sample Clothing items
        Item tshirt = new Item();
        tshirt.setName("Cotton T-Shirt");
        tshirt.setCategory("Clothing");
        tshirt.setPrice(1659.00);
        tshirt.setQuantity(50);
        tshirt.setDescription("Comfortable cotton t-shirt in various colors");
        itemService.saveItem(tshirt);
        
        Item jeans = new Item();
        jeans.setName("Denim Jeans");
        jeans.setCategory("Clothing");
        jeans.setPrice(4979.00);
        jeans.setQuantity(30);
        jeans.setDescription("Classic denim jeans, regular fit");
        itemService.saveItem(jeans);
        
        // Sample Books
        Item book1 = new Item();
        book1.setName("Spring Boot Guide");
        book1.setCategory("Books");
        book1.setPrice(3817.00);
        book1.setQuantity(8);
        book1.setDescription("Comprehensive guide to Spring Boot development");
        itemService.saveItem(book1);
        
        Item book2 = new Item();
        book2.setName("Java Programming");
        book2.setCategory("Books");
        book2.setPrice(3319.00);
        book2.setQuantity(12);
        book2.setDescription("Learn Java programming from basics to advanced");
        itemService.saveItem(book2);
        
        // Low stock item (for demonstration)
        Item headphones = new Item();
        headphones.setName("Wireless Headphones");
        headphones.setCategory("Electronics");
        headphones.setPrice(10789.00);
        headphones.setQuantity(3);
        headphones.setDescription("Premium wireless headphones with noise cancellation");
        itemService.saveItem(headphones);
        
        // Out of stock item (for demonstration)
        Item tablet = new Item();
        tablet.setName("Android Tablet");
        tablet.setCategory("Electronics");
        tablet.setPrice(24899.00);
        tablet.setQuantity(0);
        tablet.setDescription("10-inch Android tablet with high-resolution display");
        itemService.saveItem(tablet);
    }
}