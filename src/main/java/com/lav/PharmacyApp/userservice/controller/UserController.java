package com.lav.PharmacyApp.userservice.controller;


import com.lav.PharmacyApp.userservice.dto.requestdto.CreateUserRequest;
import com.lav.PharmacyApp.userservice.dto.requestdto.LoginUserRequestDto;
import com.lav.PharmacyApp.userservice.dto.requestdto.RefreshTokenRequestDto;
import com.lav.PharmacyApp.userservice.dto.responsedto.ApiResponseDto;
import com.lav.PharmacyApp.userservice.dto.responsedto.LoginResponseDto;
import com.lav.PharmacyApp.userservice.dto.responsedto.RefreshTokenResponse;
import com.lav.PharmacyApp.userservice.dto.responsedto.UserResponseDto;
import com.lav.PharmacyApp.userservice.model.RefreshToken;
import com.lav.PharmacyApp.userservice.services.JwtService;
import com.lav.PharmacyApp.userservice.services.RefreshTokenService;
import com.lav.PharmacyApp.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<ApiResponseDto> saveUser(@RequestBody  CreateUserRequest createUserRequest) {
        UserResponseDto userResponse = userService.save(createUserRequest);
        return ResponseEntity.ok(new ApiResponseDto("User Registered Succesfully",HttpStatus.CREATED.toString(), userResponse));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginUserRequestDto loginUserRequest) {

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setEmail(loginUserRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUserRequest.getEmail(),
                            loginUserRequest.getPassword()
                    )
            );

            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String accessToken = jwtService.generateToken(userDetails.getUsername());
                RefreshToken refreshToken =refreshTokenService.createRefreshToken(userDetails.getUsername());

                loginResponseDto.setAccessToken(accessToken);
                loginResponseDto.setMessage("Login Success");
                loginResponseDto.setToken(refreshToken.getToken());

                return ResponseEntity.ok(loginResponseDto);
            } else {
                loginResponseDto.setMessage("Login failed: Authentication failed");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponseDto);
            }
        } catch (AuthenticationException ex) {
            loginResponseDto.setMessage("Login failed: Incorrect username or password,"+ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponseDto);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        try {
            SecurityContextHolder.clearContext();
            return new ResponseEntity<>("User logged out successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error during logout", e);
            return new ResponseEntity<>("Error during logout", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user.getEmail());
                    log.info("Refresh token generated successfully");
                    return ResponseEntity.ok(RefreshTokenResponse.builder()
                            .accessToken(accessToken).token(refreshTokenRequest.getToken()).build());
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }

}
