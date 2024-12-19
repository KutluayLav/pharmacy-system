package com.lav.PharmacyApp.userservice.services;


import com.lav.PharmacyApp.userservice.dto.requestdto.CreateUserRequest;
import com.lav.PharmacyApp.userservice.dto.responsedto.UserResponseDto;
import com.lav.PharmacyApp.userservice.model.Role;
import com.lav.PharmacyApp.userservice.model.User;
import com.lav.PharmacyApp.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private Logger log= LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public UserResponseDto save(CreateUserRequest user) {

        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            log.info("User with email {} already exists. Returning existing user.", user.getEmail());
            return mapToUserResponseDto(existingUser.get());
        }

        log.info("Creating new user. user first name: {}", user.getFirstName());

        User newUser = new User().builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .authorities(new HashSet<>(Arrays.asList(Role.ROLE_ADMIN)))
                .build();

        User savedUser=userRepository.save(newUser);

        log.info("New user created. name: {}", savedUser.getFirstName());

        return mapToUserResponseDto(savedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByEmail(email);

        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email));

        Set<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public UserResponseDto getUserDetails() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
                return null;
            }

            String loggedInEmail = authentication.getName();

            Optional<User> userOptional = userRepository.findUserByEmail(loggedInEmail);

            User user = userOptional.orElseThrow(() ->
                    new UsernameNotFoundException("User not found with email: " + loggedInEmail));

            UserResponseDto userDetails = mapToUserResponseDto(user);
            log.info("UserDetails: {}", userDetails);
            return userDetails;
        } catch (Exception e) {
            log.error("Error getting user details", e);
            throw new RuntimeException("Error getting user details", e);
        }
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        return new UserResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAuthorities().stream()
                        .map(Role::name)
                        .collect(Collectors.toSet())
        );
    }



}
