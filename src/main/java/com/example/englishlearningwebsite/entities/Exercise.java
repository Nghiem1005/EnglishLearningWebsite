package com.example.englishlearningwebsite.entities;

import com.example.englishlearningwebsite.entities.keys.ExamDetailKey;
import com.example.englishlearningwebsite.entities.keys.ExerciseKey;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_exercise")
@IdClass(ExerciseKey.class)
public class Exercise {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "exam_id")
  private Exam exam;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "lesson_id")
  private Lesson lesson;

  @ManyToOne
  @JoinColumn(name = "student_id")
  private User student;

  //@Min(value = 0, message = "Scores must be greater 0")
  @Column(nullable = true)
  private double scores;
}
