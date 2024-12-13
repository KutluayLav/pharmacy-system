package com.lav.PharmacyApp.userservice.services;


import com.lav.PharmacyApp.userservice.model.RefreshToken;
import com.lav.PharmacyApp.userservice.model.User;
import com.lav.PharmacyApp.userservice.repository.RefreshTokenRepository;
import com.lav.PharmacyApp.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshexpiration}")
    private long refreshExpired;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String email) {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

            if (existingToken.isPresent()) {
                RefreshToken refreshToken = existingToken.get();
                refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpired));
                return refreshTokenRepository.save(refreshToken);
            } else {
                RefreshToken refreshToken = RefreshToken.builder()
                        .user(user)
                        .token(UUID.randomUUID().toString())
                        .expiryDate(Instant.now().plusMillis(refreshExpired))
                        .build();

                return refreshTokenRepository.save(refreshToken);
            }
        } else {
            throw new UsernameNotFoundException("User Not Found with Email " + email);
        }
    }

    public Optional<RefreshToken> findByToken(String token){
        return Optional.ofNullable(refreshTokenRepository.findByToken(token));
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
