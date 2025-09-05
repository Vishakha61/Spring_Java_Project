package com.example.inventory.service;

import com.example.inventory.model.Item;
import com.example.inventory.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final ItemRepository itemRepository;

    public InventoryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    // ✅ Only fetch items with stock > 0
    public List<Item> getAllItems() {
        return itemRepository.findByQuantityGreaterThan(0);
    }
}
