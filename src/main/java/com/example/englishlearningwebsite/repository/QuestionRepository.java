package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.PartExam;
import com.example.englishlearningwebsite.entities.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
  Optional<Question> findQuestionByContent(String content);
  List<Question> findQuestionsByPartExamOrderBySerial(PartExam partExam);
}
