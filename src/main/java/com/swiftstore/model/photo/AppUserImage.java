package com.swiftstore.model.photo;

import com.swiftstore.model.util.LogEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user-photo")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppUserImage extends LogEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    private Long userid;
    private Long imageId;

    public AppUserImage(Long userid, Long imageId) {
        this.userid = userid;
        this.imageId = imageId;
    }
}
