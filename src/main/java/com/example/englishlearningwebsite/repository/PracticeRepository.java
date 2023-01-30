package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Exam;
import com.example.englishlearningwebsite.entities.Practice;
import com.example.englishlearningwebsite.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {
  List<Practice> findPracticesByStudent(User student);
  List<Practice> findPracticesByStudentAndExam(User student, Exam exam);
  List<Practice> findPracticesByStudentAndExamAndScoresIsNull(User student, Exam exam);
  Optional<Practice> findPracticeByExamAndStudent(Exam exam, User student);
}
