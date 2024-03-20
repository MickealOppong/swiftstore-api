package com.swiftstore.repository;

import com.swiftstore.model.photo.AppProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppProductImageRepository extends JpaRepository<AppProductImage,Long> {

    List<AppProductImage> findByProductId(Long productid);
}
