package com.swiftstore.model.photo;

import com.swiftstore.model.util.LogEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "photos")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AppImageDetails extends LogEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String filename;
    private String path;
    private String fileType;

}
