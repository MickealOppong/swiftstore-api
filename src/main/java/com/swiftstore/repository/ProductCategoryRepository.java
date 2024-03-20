package com.swiftstore.repository;

import com.swiftstore.model.product.ProductCategory;
import com.swiftstore.model.product.ProductColours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Long> {
    List<ProductColours> findByProductId(Long productId);
}
