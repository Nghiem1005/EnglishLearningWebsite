package com.example.englishlearningwebsite.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_lesson")
public class Lesson {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 45)
  @NotNull(message = "Course name is required")
  private String name;

  private String document;

  @Min(value = 0, message = "Percent must be greater 0")
  private int serial;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

  @CreationTimestamp
  private Date createDate;

  @UpdateTimestamp
  private Date updateDate;

  /*@OneToMany(mappedBy="lesson", orphanRemoval=true, cascade={CascadeType.REMOVE})
  private List<Topic> topics;*/

/*  @OneToMany(mappedBy="lesson", orphanRemoval=true, cascade={CascadeType.REMOVE})
  private List<Schedule> schedules;*/
}
