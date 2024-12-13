package com.lav.PharmacyApp.userservice.controller;


import com.lav.PharmacyApp.userservice.dto.requestdto.CreateUserRequest;
import com.lav.PharmacyApp.userservice.dto.responsedto.ApiResponseDto;
import com.lav.PharmacyApp.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<ApiResponseDto> saveUser(@RequestBody  CreateUserRequest createUserRequest) {




    }
}
