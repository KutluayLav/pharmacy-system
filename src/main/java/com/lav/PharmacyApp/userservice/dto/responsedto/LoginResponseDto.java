package com.lav.PharmacyApp.userservice.dto.responsedto;

import lombok.Data;

@Data
public class LoginResponseDto {

    private String message;

    private String email;

    private String accessToken;

    private String token;


}
