package com.auto.care.autoserviceapisrc.entity;

import com.auto.care.autoserviceapisrc.util.UserTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserTypeEnum userType;
}
