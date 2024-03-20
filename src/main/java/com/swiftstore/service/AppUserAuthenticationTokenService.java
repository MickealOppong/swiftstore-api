package com.swiftstore.service;

import com.swiftstore.model.User.AppUserDetails;
import com.swiftstore.repository.AppUserDetailsRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AppUserAuthenticationTokenService {

    private JwtEncoder jwtEncoder;
    private AppUserDetailsRepository appUserRepository;

    public AppUserAuthenticationTokenService(JwtEncoder jwtEncoder, AppUserDetailsRepository appUserRepository) {
        this.jwtEncoder = jwtEncoder;
        this.appUserRepository = appUserRepository;
    }

    public String generateToken(Authentication authentication){
        String authority = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toString();
                JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                        .subject(authentication.getName())
                        .claim(authority,"scope")
                .build();
                return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public String generateToken(String username){
        String authority = appUserRepository.findByUsername(username).map(AppUserDetails::getRoles).toString();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .subject(username)
                .claim(authority,"scope")
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
