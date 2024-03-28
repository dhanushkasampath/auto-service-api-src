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
public class Appointment extends AbstractEntity{

    @Id
    private Long appointmentId;

    private LocalDateTime timestamp;

    private LocalDateTime appointmentDate1;

    private LocalDateTime appointmentDate2;

    @Enumerated(EnumType.STRING)// When we use like this db doesn't allow to enter values other than the given values
    private AppointmentStatusEnum status;

}
