package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Answer;
import com.example.englishlearningwebsite.entities.Question;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
  Optional<Answer> findAnswerByContentAndQuestion(String content, Question question);
}
