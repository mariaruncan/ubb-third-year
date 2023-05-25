package com.university.iibackend.repository;

import com.university.iibackend.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

}
