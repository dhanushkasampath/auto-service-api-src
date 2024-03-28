package com.auto.care.autoserviceapisrc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "service_type")
public class ServiceType {
    @Id
    private Long serviceTypeId;
    private String name;
    private String description;
}
