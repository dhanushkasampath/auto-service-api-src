package com.auto.care.autoserviceapisrc.entity;

import com.auto.care.autoserviceapisrc.util.ServiceStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "service")
public class Service extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalCost;
    @Enumerated(EnumType.STRING)
    private ServiceStatusEnum status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointmentId")
    private Appointment appointment;

//    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL)
//    private List<ServiceType> serviceTypeList;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "service_service_type",
            joinColumns = {@JoinColumn(name = "service_id")},
            inverseJoinColumns = {@JoinColumn(name = "service_type_id")})
    private List<ServiceType> serviceTypeList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "service_technician",
            joinColumns = {@JoinColumn(name = "service_id")},
            inverseJoinColumns = {@JoinColumn(name = "technician_id")})
    private List<Technician> technicianList;

}
