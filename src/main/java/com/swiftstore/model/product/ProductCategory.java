package com.swiftstore.model.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swiftstore.model.util.LogEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory extends LogEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long quantity;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "fk_productId",referencedColumnName = "id")
    private Product product;
}



