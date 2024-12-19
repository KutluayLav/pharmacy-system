package com.lav.PharmacyApp.userservice.dto.requestdto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginUserRequestDto {

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
