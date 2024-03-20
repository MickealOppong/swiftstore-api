package com.swiftstore.controller;

import com.swiftstore.exceptions.TokenNotFoundException;
import com.swiftstore.model.User.AuthRequest;
import com.swiftstore.model.User.JwtResponse;
import com.swiftstore.model.util.RefreshToken;
import com.swiftstore.model.User.RefreshTokenRequest;
import com.swiftstore.service.AppUserAuthenticationTokenService;
import com.swiftstore.service.AppUserRefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthRequestController {


    private AppUserRefreshTokenService appUserRefreshTokenService;
    private AppUserAuthenticationTokenService tokenService;
    private AuthenticationManager authenticationManager;

    public AuthRequestController(AppUserRefreshTokenService appUserRefreshTokenService,
                                 AppUserAuthenticationTokenService tokenService,
                                 AuthenticationManager authenticationManager) {
        this.appUserRefreshTokenService = appUserRefreshTokenService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping
    public String welcome(){
        return "Welcome to user authentication endpoint";
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authUser(@RequestBody AuthRequest authRequest){
        try{
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(),authRequest.password()));
            if(authentication.isAuthenticated()){
                RefreshToken refreshToken = appUserRefreshTokenService.createToken(authentication.getName());
                JwtResponse jwtResponse = JwtResponse.builder()
                        .token(tokenService.generateToken(authentication))
                        .accessToken(refreshToken.getToken())
                        .build();
                return ResponseEntity.ok().body(jwtResponse);
            }
        }catch (TokenNotFoundException e){

        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return appUserRefreshTokenService.findToken(refreshTokenRequest.token())
                .map(appUserRefreshTokenService::isTokenExpired)
                .map(RefreshToken::getAppUser)
                .map(appUser -> {
                    String accessToken = tokenService.generateToken(appUser.getUsername());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.token())
                            .build();
                }).get();
    }



    @DeleteMapping("/logout")
    public void removeToken(@RequestParam String token){
      appUserRefreshTokenService.removeToken(token);
    }
}
