package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.Topic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
  Page<Topic> findTopicByLesson(Lesson lesson, Pageable pageable);
  Optional<Topic> findTopicByTitle(String title);
  List<Topic> findTopicByLesson(Lesson lesson);
}
