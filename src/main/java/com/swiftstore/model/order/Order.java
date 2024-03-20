package com.swiftstore.model.order;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    class OrderItem{
        private Long id;
        private Long productId;
        private String productTitle;
        private String productColour;
        private BigDecimal price;
    }
}
