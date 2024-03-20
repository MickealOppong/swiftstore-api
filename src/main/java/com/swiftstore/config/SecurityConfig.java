package com.swiftstore.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.swiftstore.model.User.AppUserDetails;
import com.swiftstore.model.User.Roles;
import com.swiftstore.model.util.RsaKeyProperties;
import com.swiftstore.repository.AppUserDetailsRepository;
import com.swiftstore.impl.AppImageStorageImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final RsaKeyProperties rsaKey;
    private AppUserDetailsRepository appUserRepository;
    private AppImageStorageImpl imageStorageService;



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
       return NimbusJwtDecoder.withPublicKey(rsaKey.publicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKey.publicKey()).privateKey(rsaKey.privateKey()).build();
        JWKSource<SecurityContext> jwt = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwt);
    }

    @Bean
    public UserDetailsService userDetailsService(){
       return username -> appUserRepository.findByUsername(username)
               .orElseThrow(()-> new UsernameNotFoundException(username+" does not exist"));
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
      return httpSecurity.csrf(csrf->csrf.disable())
              .cors(Customizer.withDefaults())
              .authorizeHttpRequests(request->
                      request.requestMatchers("/api/user","/api/user/**","api/photo","/api/photo/**","/api/product","/api/product/**").authenticated()
                      .requestMatchers("/api/auth","/api/auth/**").permitAll()
                      .requestMatchers("/api/register","/api/register/**").permitAll())
              .oauth2ResourceServer(rs->rs.jwt(Customizer.withDefaults()))
              .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .httpBasic(Customizer.withDefaults())
              .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:5174"));
        config.setAllowedMethods(Arrays.asList("POST","GET","DELETE","PUT","PATCH","OPTIONS","HEAD"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }

    @Bean
    public CommandLineRunner init(){
        return args -> {
            AppUserDetails appUserDetails = new AppUserDetails("epps@mail.com",passwordEncoder().encode("password"));
            Roles role = new Roles("USER");
            appUserDetails.setRoles(role);
            appUserRepository.save(appUserDetails);

            imageStorageService.init();
        };
    }
}

