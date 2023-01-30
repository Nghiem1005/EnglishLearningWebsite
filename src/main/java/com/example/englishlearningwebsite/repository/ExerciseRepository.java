package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Exam;
import com.example.englishlearningwebsite.entities.Exercise;
import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
  List<Exercise> findExercisesByLesson(Lesson lesson);
  List<Exercise> findExercisesByLessonAndExamAndStudentIsNull(Lesson lesson, Exam exam);
  List<Exercise> findExercisesByLessonAndExam(Lesson lesson, Exam exam);
  Optional<Exercise> findExerciseByExamAndLessonAndStudent(Exam exam, Lesson lesson, User student);
}
