package com.swiftstore.repository;

import com.swiftstore.model.photo.AppImageDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppImageDetailsRepository extends JpaRepository<AppImageDetails,Long> {
    Optional<AppImageDetails> findByPath(String path);
}
