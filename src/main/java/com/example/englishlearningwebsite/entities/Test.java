package com.example.englishlearningwebsite.entities;

import com.example.englishlearningwebsite.entities.keys.ExamDetailKey;
import com.example.englishlearningwebsite.entities.keys.TestKey;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_test")
@IdClass(TestKey.class)
public class Test {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "exam_id")
  private Exam exam;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "course_id")
  private Course course;

  @ManyToOne(optional = false)
  @JoinColumn(name = "student_id")
  private User student;

  @Temporal(TemporalType.DATE)
  private Date startDay;

  @Min(value = 0, message = "Scores must be greater 0")
  private double scores;
}
