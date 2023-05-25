package com.university.iibackend.service;

import com.university.iibackend.model.Quiz;
import com.university.iibackend.model.QuizAnswer;
import com.university.iibackend.model.User;
import com.university.iibackend.repository.QuizAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizAnswerService {
    private final QuizAnswerRepository repo;

    public int getMaxScoreOnQuizForUser(User user, Quiz quiz) {
        List<QuizAnswer> ans = repo.findByUserAndQuiz(user, quiz);
        return ans.stream()
                .map(QuizAnswer::getScore)
                .reduce(0, Integer::max);
    }
}
