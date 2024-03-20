package com.swiftstore.service;

import com.swiftstore.model.util.RefreshToken;
import com.swiftstore.repository.AppUserDetailsRepository;
import com.swiftstore.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserRefreshTokenService {


    private RefreshTokenRepository refreshTokenRepository;
    private AppUserDetailsRepository appUserRepository;

    public AppUserRefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                                      AppUserDetailsRepository appUserRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.appUserRepository = appUserRepository;
    }

    public Optional<RefreshToken> findToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .appUser(appUserRepository.findByUsername(username).get())
                .expirationTime(Instant.now().plus(1, ChronoUnit.HOURS))
                .token(UUID.randomUUID().toString())
                .build();
       return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken isTokenExpired(RefreshToken refreshToken){
        if(refreshToken.getExpirationTime().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);
        }
        return refreshToken;
    }

    public void removeToken(String refreshToken){
        Optional<RefreshToken> token = refreshTokenRepository.findByToken(refreshToken);
        token.ifPresent(value -> refreshTokenRepository.delete(value));
    }
}
