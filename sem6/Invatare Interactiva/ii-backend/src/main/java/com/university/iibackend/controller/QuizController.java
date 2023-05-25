package com.university.iibackend.controller;

import com.university.iibackend.model.Quiz;
import com.university.iibackend.service.QuizService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
@CrossOrigin
public class QuizController {
    private final QuizService service;

    @GetMapping
    public ResponseEntity<List<Quiz>> getQuizzes() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<?> saveResponse(@RequestParam Integer userId,
            @RequestParam Integer quizId, @RequestParam Integer score) {
        try {
            service.saveScore(userId, quizId, score);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save quiz response: " + e.getMessage());
        }
    }
}
