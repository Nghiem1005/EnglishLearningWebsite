package com.example.englishlearningwebsite.entities;

import com.example.englishlearningwebsite.entities.keys.PracticeKey;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_practice")
@IdClass(PracticeKey.class)
public class Practice {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "exam_id")
  private Exam exam;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "student_id")
  private User student;

  @Min(value = 0, message = "Scores must be greater 0")
  private double scores;
}
