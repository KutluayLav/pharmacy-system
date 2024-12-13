package com.lav.PharmacyApp.userservice.dto.requestdto;

import jakarta.annotation.Nonnull;
import lombok.Data;

@Data
public class LoginUserRequestDto {

    @Nonnull
    private String email;
    @Nonnull
    private String password;
}
