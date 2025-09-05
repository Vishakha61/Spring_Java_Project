package com.example.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // use Long everywhere, not int

    private String name;
    private String category;
    private double price;
    private int quantity;  // keep primitive int
}
