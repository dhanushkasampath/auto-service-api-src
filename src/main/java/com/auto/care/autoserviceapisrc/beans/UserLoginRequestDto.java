package com.auto.care.autoserviceapisrc.beans;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequestDto implements Serializable {

    private static final long serialVersionUID = 5595304138483883021L;

    @NotNull(message = "User authentication is invalid. Please try again.")
    @NotEmpty(message = "User authentication is invalid. Please try again.")
    private String userName;

    @NotNull(message = "User authentication is invalid. Please try again.")
    @NotEmpty(message = "User authentication is invalid. Please try again.")
    private String password;
}
