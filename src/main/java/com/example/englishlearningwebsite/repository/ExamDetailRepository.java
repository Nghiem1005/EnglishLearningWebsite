package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Exam;
import com.example.englishlearningwebsite.entities.ExamDetail;
import com.example.englishlearningwebsite.entities.PartExam;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamDetailRepository extends JpaRepository<ExamDetail, Long> {
  List<ExamDetail> findExamDetailsByExamOrderBySerial(Exam exam);
  Optional<ExamDetail> findExamDetailByExamAndPartExam(Exam exam, PartExam partExam);
  List<ExamDetail> findExamDetailsByPartExam(PartExam partExam);
}
