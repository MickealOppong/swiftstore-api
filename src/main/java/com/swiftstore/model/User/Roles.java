package com.swiftstore.model.User;

import com.swiftstore.model.util.LogEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
public class Roles extends LogEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;

    public Roles() {
    }

    public Roles(String role) {
        this.role = role;
    }
}
