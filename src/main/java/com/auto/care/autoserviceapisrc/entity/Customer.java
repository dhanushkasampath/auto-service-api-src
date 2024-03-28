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
@Table(name = "customer")
public class Customer {
    @Id
    private Long customerId;

    private String name;

    private String phone;
    private String address;
    private String email;
}
