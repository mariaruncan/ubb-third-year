package com.university.iibackend.service;

import com.university.iibackend.model.Quiz;
import com.university.iibackend.model.QuizAnswer;
import com.university.iibackend.model.User;
import com.university.iibackend.repository.QuizAnswerRepository;
import com.university.iibackend.repository.QuizRepository;
import com.university.iibackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizAnswerRepository quizAnswerRepository;

    public List<Quiz> getAll() {
        return quizRepository.findAll();
    }

    @Transactional
    public void saveScore(int userId, int quizId, int score) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + quizId));

        QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswer.setUser(user);
        quizAnswer.setQuiz(quiz);
        quizAnswer.setScore(score);

        quizAnswerRepository.save(quizAnswer);
    }

    public int getTotalScoreForUser(User user) {
        List<QuizAnswer> userQuizAnswers = quizAnswerRepository.findByUser(user);
        Map<Quiz, Integer> quizScores = new HashMap<>();

        for (QuizAnswer quizAnswer : userQuizAnswers) {
            Quiz quiz = quizAnswer.getQuiz();
            Integer currentScore = quizAnswer.getScore();
            Integer previousScore = quizScores.get(quiz);

            if (previousScore == null || currentScore > previousScore) {
                quizScores.put(quiz, currentScore);
            }
        }

        return quizScores.values().stream().mapToInt(Integer::intValue).sum();
    }
}
