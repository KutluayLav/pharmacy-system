package com.lav.PharmacyApp.userservice.dto.requestdto;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginUserRequestDto {

    @NotNull
    private String email;
    @NotNull
    private String password;
}
