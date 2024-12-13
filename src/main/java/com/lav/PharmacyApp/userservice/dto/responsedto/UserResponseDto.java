package com.lav.PharmacyApp.userservice.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String firstName;

    private String lastName;

    private String email;

    private Set<String> roles;
}
