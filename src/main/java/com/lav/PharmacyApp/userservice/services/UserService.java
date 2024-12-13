package com.lav.PharmacyApp.userservice.services;


import com.lav.PharmacyApp.userservice.dto.requestdto.CreateUserRequest;
import com.lav.PharmacyApp.userservice.dto.responsedto.UserResponse;
import com.lav.PharmacyApp.userservice.model.Role;
import com.lav.PharmacyApp.userservice.model.User;
import com.lav.PharmacyApp.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private Logger log= LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public UserResponse save(CreateUserRequest user) {

        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());

        if (existingUser.isPresent()) {

            log.info("User with email {} already exists. Returning existing user.", user.getEmail());

            return existingUser.get();
        }

        User newUser = new User().builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .authorities(new HashSet<>(Arrays.asList(Role.ROLE_ADMIN)))
                .build();

        return userRepository.save(newUser);
    }
}
