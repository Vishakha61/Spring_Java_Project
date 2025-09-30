package com.example.inventory.runner;

import com.example.inventory.model.Item;
import com.example.inventory.model.Sales;
import com.example.inventory.service.BillingService;
import com.example.inventory.service.InventoryService;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

// @Component  // Commented out to disable console runner for web-only mode
public class ConsoleRunner implements CommandLineRunner {

    private final InventoryService inventoryService;
    private final BillingService billingService;

    public ConsoleRunner(InventoryService inventoryService, BillingService billingService) {
        this.inventoryService = inventoryService;
        this.billingService = billingService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Inventory & Billing System =====");
            System.out.println("1. Add Item");
            System.out.println("2. View Items");
            System.out.println("3. Generate Bill");
            System.out.println("4. View All Sales");
            System.out.println("5. Sales Report by Category");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter item name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter category: ");
                        String category = scanner.nextLine();
                        System.out.print("Enter price: ");
                        double price = scanner.nextDouble();
                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();

                        Item item = Item.builder()
                                .name(name)
                                .category(category)
                                .price(price)
                                .quantity(quantity)
                                .build();

                        inventoryService.addItem(item);
                        System.out.println("✅ Item added successfully.");
                    }
                    case 2 -> {
                        List<Item> items = inventoryService.getAllItems();
                        List<Item> availableItems = items.stream()
                                .filter(i -> i.getQuantity() > 0)
                                .toList();

                        if (availableItems.isEmpty()) {
                            System.out.println("\n--- Inventory ---");
                            System.out.println("⚠️ No items available in inventory.\n");
                        } else {
                            System.out.println("\n--- Inventory ---");
                            System.out.printf("%-5s %-15s %-15s %-10s %-10s%n",
                                    "ID", "Name", "Category", "Price", "Stock");
                            System.out.println("--------------------------------------------------------");
                            for (Item i : availableItems) {
                                System.out.printf("%-5d %-15s %-15s %-10.2f %-10d%n",
                                        i.getId(), i.getName(), i.getCategory(),
                                        i.getPrice(), i.getQuantity());
                            }
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter Item ID: ");
                        Long itemId = scanner.nextLong();
                        System.out.print("Enter quantity: ");
                        int qty = scanner.nextInt();
                        Sales sale = billingService.generateBill(itemId, qty);
                        System.out.println("✅ Bill generated: " + sale);
                    }
                    case 4 -> {
                        List<Sales> salesList = billingService.getAllSales();
                        System.out.println("\n--- All Sales ---");
                        if (salesList.isEmpty()) {
                            System.out.println("⚠️ No sales recorded yet.\n");
                        } else {
                            salesList.forEach(System.out::println);
                        }
                    }
                    case 5 -> {
                        Map<String, Double> report = billingService.getSalesReportByCategory();
                        System.out.println("\n--- Sales Report by Category ---");
                        if (report.isEmpty()) {
                            System.out.println("⚠️ No sales available for reporting.\n");
                        } else {
                            report.forEach((cat, total) ->
                                    System.out.println(cat + " -> " + total));
                        }
                    }
                    case 6 -> {
                        System.out.println("👋 Exiting system. Goodbye!");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error: " + e.getMessage());
            }
        }
    }
}
