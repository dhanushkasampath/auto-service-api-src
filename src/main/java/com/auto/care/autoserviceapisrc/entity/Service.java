package com.auto.care.autoserviceapisrc.entity;

import com.auto.care.autoserviceapisrc.util.ServiceStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "service")
public class Service extends AbstractEntity {
    @Id
    private Long serviceId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    private ServiceStatusEnum status;
}
