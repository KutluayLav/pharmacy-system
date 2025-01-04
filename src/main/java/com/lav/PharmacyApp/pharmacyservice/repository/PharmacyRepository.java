package com.lav.PharmacyApp.pharmacyservice.repository;

import com.lav.PharmacyApp.pharmacyservice.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

}
