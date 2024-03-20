package com.swiftstore.model.product;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swiftstore.model.util.LogEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends LogEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String company;
    private Long availableQuantity;
    private BigDecimal price;
    private boolean isFeatured;

    @JsonManagedReference
    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    private List<ProductColours> productColours = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy ="product",fetch = FetchType.EAGER)
    private List<ProductCategory> productCategory = new ArrayList<>();

    @Transient
    private List<String> imgs = new ArrayList<>();

    public Product(String title, String description, String company,
                   Long availableQuantity, BigDecimal price,boolean isFeatured,
                   List<ProductColours> colours, List<ProductCategory> productCategory) {
        this.title= title;
        this.description = description;
        this.company = company;
        this.availableQuantity = availableQuantity;
        this.price = price;
        this.isFeatured = isFeatured;
        this.productColours = colours;
        this.productCategory = productCategory;
    }
}
