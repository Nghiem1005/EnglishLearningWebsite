package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Document;
import com.example.englishlearningwebsite.entities.PartExam;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
  Optional<Document> findDocumentByPartExam(PartExam partExam);
}
