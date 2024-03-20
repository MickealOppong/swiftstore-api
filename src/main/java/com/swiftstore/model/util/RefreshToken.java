package com.swiftstore.model.util;

import com.swiftstore.model.User.AppUserDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "token")
@Data
@Builder
@AllArgsConstructor
public class RefreshToken extends LogEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    private Instant expirationTime;
    @OneToOne
    @JoinColumn(name = "userid",referencedColumnName = "id")
    private AppUserDetails appUser;


    public RefreshToken(String token,AppUserDetails appUser) {
        this.token = token;
        this.appUser = appUser;
    }
}
