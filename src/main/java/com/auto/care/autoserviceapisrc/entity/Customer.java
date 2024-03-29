package com.auto.care.autoserviceapisrc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String name;
    private String phone;
    private String address;
    private String email;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Appointment> appointmentList;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Vehicle> vehicleList;
}
