package com.swiftstore.model.photo;

import com.swiftstore.model.util.LogEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppProductImage extends LogEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long productId;
    private Long imageId;

    public AppProductImage(Long productId, Long imageId) {
        this.productId = productId;
        this.imageId = imageId;
    }
}
