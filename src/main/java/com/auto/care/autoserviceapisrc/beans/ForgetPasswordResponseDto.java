package com.auto.care.autoserviceapisrc.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 3780732672946107251L;

    private String email;

}
