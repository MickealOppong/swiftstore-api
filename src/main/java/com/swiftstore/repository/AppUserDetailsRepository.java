package com.swiftstore.repository;

import com.swiftstore.model.User.AppUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserDetailsRepository extends JpaRepository<AppUserDetails,Long> {
    Optional<AppUserDetails> findByUsername(String username);
}
