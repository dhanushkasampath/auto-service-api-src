package com.auto.care.autoserviceapisrc.beans;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class UserLoginRequestDto implements Serializable {
    private static final long serialVersionUID = 5595304138483883021L;
    @NotNull(message = "User authentication is invalid. Please try again.")
    @NotEmpty(message = "User authentication is invalid. Please try again.")
    private String userName;

    @NotNull(message = "User authentication is invalid. Please try again.")
    @NotEmpty(message = "User authentication is invalid. Please try again.")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
