package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.PartExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartExamRepository extends JpaRepository<PartExam, Long> {

}
