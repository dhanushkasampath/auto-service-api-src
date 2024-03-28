package com.auto.care.autoserviceapisrc.entity;

import com.auto.care.autoserviceapisrc.util.AppointmentStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "appointment")
public class Appointment {

    @Id
    private Long id;

    private LocalDateTime timestamp;

    private LocalDateTime appointmentDate1;

    private LocalDateTime appointmentDate2;

    @Enumerated(EnumType.STRING)
    private AppointmentStatusEnum status;

}
