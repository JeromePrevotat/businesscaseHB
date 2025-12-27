package com.humanbooster.businesscase.controller;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/geocode")
@RequiredArgsConstructor
public class NominatimController {

    @GetMapping
    public ResponseEntity<String> geocode(@RequestParam String q) {

        String url = UriComponentsBuilder
                .fromUriString("https://nominatim.openstreetmap.org/search")
                .queryParam("q", UriUtils.encode(q, StandardCharsets.UTF_8))
                .queryParam("format", "json")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Electricity Business - End of Studies Project/1.0 (jer.prevotat@gmail.com)");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
        );

        return ResponseEntity.ok(response.getBody());
    }
}
