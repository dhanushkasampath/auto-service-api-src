package com.auto.care.autoserviceapisrc.entity;

import com.auto.care.autoserviceapisrc.util.TechnicianTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "technician")
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long technicianId;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private TechnicianTypeEnum type;
    @ManyToMany(mappedBy = "technicianList")
    private List<Service> serviceList;
}
