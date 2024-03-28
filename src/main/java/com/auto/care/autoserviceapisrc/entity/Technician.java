package com.auto.care.autoserviceapisrc.entity;

import com.auto.care.autoserviceapisrc.util.TechnicianTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "technician")
public class Technician {
    @Id
    private Long technicianId;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private TechnicianTypeEnum type;
}
