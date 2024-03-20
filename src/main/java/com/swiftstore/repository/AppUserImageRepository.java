package com.swiftstore.repository;

import com.swiftstore.model.photo.AppUserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserImageRepository extends JpaRepository<AppUserImage,Long> {

    Optional<AppUserImage> findByUserid(Long userid);
}
