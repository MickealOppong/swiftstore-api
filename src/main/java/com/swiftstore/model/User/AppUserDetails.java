package com.swiftstore.model.User;

import com.swiftstore.model.util.LogEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class AppUserDetails extends LogEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String fullname;

    @Column(name = "isBlocked")
    private boolean isAccountNonLocked = true;

    @Column(name = "isExpired")
    private boolean isAccountNonExpired = true;

    @Column(name = "credentialExpired")
    private boolean isCredentialsNonExpired = true;

    private boolean isEnabled = true;


    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "roleId",referencedColumnName = "id")
    private Roles roles;

    @Transient
    private String imageUrl;


    public AppUserDetails() {
    }

    public AppUserDetails(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new Roles("USER");
    }

    public AppUserDetails(String username, String password, Roles roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of (new SimpleGrantedAuthority(roles.getRole()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
