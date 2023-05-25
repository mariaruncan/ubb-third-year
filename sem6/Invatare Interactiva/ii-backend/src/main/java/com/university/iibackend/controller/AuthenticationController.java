package com.university.iibackend.controller;

import com.university.iibackend.model.dto.AuthenticationRequest;
import com.university.iibackend.model.dto.AuthenticationResponse;
import com.university.iibackend.model.dto.RegisterRequest;
import com.university.iibackend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(service.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }
}
