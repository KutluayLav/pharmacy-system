package com.lav.PharmacyApp.pharmacyservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {

    private String name;
    private String dist;
    private String address;
    private String phone;
    private String loc;
}
