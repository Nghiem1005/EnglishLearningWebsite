package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.Exam;
import com.example.englishlearningwebsite.entities.Test;
import com.example.englishlearningwebsite.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
  List<Test> findTestsByCourse(Course course);
  List<Test> findTestsByCourseAndExam(Course course, Exam exam);
  List<Test> findTestsByCourseAndExamAndStudentIsNull(Course course, Exam exam);
  Optional<Test> findTestByCourseAndExamAndStudent(Course course, Exam exam, User student);
}
