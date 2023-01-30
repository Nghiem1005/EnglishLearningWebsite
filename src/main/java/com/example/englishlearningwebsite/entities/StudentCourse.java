package com.example.englishlearningwebsite.entities;

import com.example.englishlearningwebsite.entities.keys.StudentCourseKey;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_student_course")
@IdClass(StudentCourseKey.class)
public class StudentCourse {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "course_id")
  private Course course;

  @NotNull(message = "Course progress is required")
  private int progress;

  @CreationTimestamp
  private Date createDate;

  @UpdateTimestamp
  private Date updateDate;
}
