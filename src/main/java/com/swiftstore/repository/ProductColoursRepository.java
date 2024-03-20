package com.swiftstore.repository;

import com.swiftstore.model.product.ProductColours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductColoursRepository extends JpaRepository<ProductColours,Long> {
    List<ProductColours> findByProductId(Long productId);
}
