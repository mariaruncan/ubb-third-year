package com.university.iibackend.service;

import com.university.iibackend.model.Role;
import com.university.iibackend.model.User;
import com.university.iibackend.model.dto.AuthenticationRequest;
import com.university.iibackend.model.dto.AuthenticationResponse;
import com.university.iibackend.model.dto.ChangePasswordRequest;
import com.university.iibackend.model.dto.RegisterRequest;
import com.university.iibackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        var user = userRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow();
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }
}
