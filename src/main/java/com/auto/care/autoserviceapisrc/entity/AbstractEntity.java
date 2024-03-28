package com.auto.care.autoserviceapisrc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class AbstractEntity {

    @CreationTimestamp
    @Column(name = "created_date",
            updatable = false,
            nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date",
            nullable = false)
    private LocalDateTime lastModifiedDate;

}