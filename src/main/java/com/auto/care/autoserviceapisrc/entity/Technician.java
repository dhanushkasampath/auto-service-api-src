package com.auto.care.autoserviceapisrc.entity;

import com.auto.care.autoserviceapisrc.util.TechnicianTypeEnum;
import jakarta.persistence.*;
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
    @ManyToMany(mappedBy = "technician")
    private List<Service> serviceList;
}
