package com.swiftstore.model.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class LogEntity {

    @CreatedBy
    @Column(updatable = false,insertable = true)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false,insertable = true)
    private LocalDateTime createAt;

    @LastModifiedBy
    @Column(updatable = true,insertable = false)
    private String modifiedBy;

    @LastModifiedDate
    @Column(updatable = true,insertable = false)
    private LocalDateTime modifiedAt;
}
