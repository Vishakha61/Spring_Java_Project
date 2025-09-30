package com.example.inventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private int quantitySold;
    
    @Column(nullable = false)
    private double totalAmount;
    
    @Column(nullable = false)
    private LocalDateTime saleDate;
}
