package com.auto.care.autoserviceapisrc.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto implements Serializable {
    private static final long serialVersionUID = -4444225008565656895L;
    private String token;

}
