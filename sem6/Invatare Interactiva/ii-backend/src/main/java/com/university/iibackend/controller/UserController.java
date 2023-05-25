package com.university.iibackend.controller;

import com.university.iibackend.model.User;
import com.university.iibackend.model.dto.ChangePasswordRequest;
import com.university.iibackend.model.dto.UserResponse;
import com.university.iibackend.service.AuthenticationService;
import com.university.iibackend.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final AuthenticationService authenticationService;
    private final QuizService quizService;

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email) {
        User user = authenticationService.getUserByEmail(email);
        int score = quizService.getTotalScoreForUser(user);
        return ResponseEntity.ok(
                UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .score(score)
                        .build()
        );
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        authenticationService.changePassword(changePasswordRequest);
    }
}
