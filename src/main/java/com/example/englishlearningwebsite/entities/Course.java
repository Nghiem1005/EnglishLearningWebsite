package com.example.englishlearningwebsite.entities;

import com.example.englishlearningwebsite.entities.enums.CourseType;
import com.example.englishlearningwebsite.entities.enums.Level;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_course")
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 45, unique = true)
  @NotNull(message = "Course name is required")
  private String name;

  private BigDecimal price;

  private String description;

  private String thumbnail;

  @Enumerated(EnumType.ORDINAL)
  private Level level;

  @Enumerated(EnumType.STRING)
  private CourseType courseType;

  @Temporal(TemporalType.DATE)
  private Date startDay;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  private User teacher;

  @CreationTimestamp
  private Date createDate;

  @UpdateTimestamp
  private Date updateDate;

  @OneToMany(mappedBy="course", orphanRemoval=true, cascade={CascadeType.REMOVE})
  private List<Feedback> feedbacks;

  @ElementCollection
  private List<String> document;
}
