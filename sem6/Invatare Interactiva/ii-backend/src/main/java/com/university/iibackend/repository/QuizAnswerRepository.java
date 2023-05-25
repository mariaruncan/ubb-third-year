package com.university.iibackend.repository;

import com.university.iibackend.model.Quiz;
import com.university.iibackend.model.QuizAnswer;
import com.university.iibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Integer> {
    List<QuizAnswer> findByUser(User user);

    List<QuizAnswer> findByUserAndQuiz(User user, Quiz quiz);
}
