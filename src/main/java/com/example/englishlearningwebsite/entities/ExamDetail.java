package com.example.englishlearningwebsite.entities;

import com.example.englishlearningwebsite.entities.keys.ExamDetailKey;
import com.example.englishlearningwebsite.entities.keys.StudentCourseKey;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_exam_detail")
@IdClass(ExamDetailKey.class)
public class ExamDetail {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "exam_id")
  private Exam exam;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "part_exam_id")
  private PartExam partExam;

  @NotNull(message = "Serial exam is required")
  @Min(value = 0, message = "Percent must be greater 0")
  private int serial;
}
