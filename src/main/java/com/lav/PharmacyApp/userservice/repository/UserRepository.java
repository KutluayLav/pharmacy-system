package com.lav.PharmacyApp.userservice.repository;

import com.lav.PharmacyApp.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {



}
