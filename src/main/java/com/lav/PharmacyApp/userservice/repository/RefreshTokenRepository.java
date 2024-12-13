package com.lav.PharmacyApp.userservice.repository;

import com.lav.PharmacyApp.userservice.model.RefreshToken;
import com.lav.PharmacyApp.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByToken(String token);

    Optional<RefreshToken> findByUser(User user);
}
