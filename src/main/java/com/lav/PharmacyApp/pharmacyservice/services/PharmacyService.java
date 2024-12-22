package com.lav.PharmacyApp.pharmacyservice.services;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PharmacyService {

    private final RestTemplate restTemplate;

    public PharmacyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String getDutyPharmacyByCityAndDistrict(String city, String district) {
        String url = "https://api.collectapi.com/health/dutyPharmacy?ilce=" + district + "&il=" + city;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "apikey 2Y9K5hs28EbqlZid1983Z4:42jHYv4xdZxEHeWwpoG0xp"); // API anahtarınızı buraya ekleyin

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    public String getDistrictList(String city) {

        String url = "https://api.collectapi.com/health/districtList?il=istanbul";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "apikey 2Y9K5hs28EbqlZid1983Z4:42jHYv4xdZxEHeWwpoG0xp");

        HttpEntity<String> entity = new HttpEntity<>(headers);
ß
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

}
