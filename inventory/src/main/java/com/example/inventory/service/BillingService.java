package com.example.inventory.service;

import com.example.inventory.model.Item;
import com.example.inventory.model.Sales;
import com.example.inventory.repository.ItemRepository;
import com.example.inventory.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BillingService {

    private final ItemRepository itemRepository;
    private final SalesRepository salesRepository;

    @Autowired
    public BillingService(ItemRepository itemRepository, SalesRepository salesRepository) {
        this.itemRepository = itemRepository;
        this.salesRepository = salesRepository;
    }

    // ✅ Generate a bill for an item purchase
    public Sales generateBill(Long itemId, int quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        if (item.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for item: " + item.getName());
        }

        double totalAmount = item.getPrice() * quantity;

        // Update stock
        item.setQuantity(item.getQuantity() - quantity);
        itemRepository.save(item);

        // Save sales entry
        Sales sale = Sales.builder()
                .item(item)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .saleDate(LocalDate.now())
                .build();

        return salesRepository.save(sale);
    }

    // ✅ Get all sales
    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    // ✅ Sales report by category
    public Map<String, Double> getSalesReportByCategory() {
        List<Sales> salesList = salesRepository.findAll();

        return salesList.stream()
                .collect(Collectors.groupingBy(
                        sale -> sale.getItem().getCategory(),
                        Collectors.summingDouble(Sales::getTotalAmount)
                ));
    }
}
