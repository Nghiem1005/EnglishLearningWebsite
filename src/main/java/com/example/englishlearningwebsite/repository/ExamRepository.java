package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

}
