package com.lav.PharmacyApp.userservice.dto.responsedto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {

    private String message;

    private String status;

    private Object data;
}
