package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.Feedback;
import com.example.englishlearningwebsite.entities.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
  List<Feedback> findFeedbacksByUser(User user);
  Page<Feedback> findFeedbacksByCourse(Pageable pageable, Course course);
  List<Feedback> findFeedbacksByCourse(Course course);
}
