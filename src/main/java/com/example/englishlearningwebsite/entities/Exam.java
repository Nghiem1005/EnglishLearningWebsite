package com.example.englishlearningwebsite.entities;

import com.example.englishlearningwebsite.entities.enums.PartExamType;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_exam")
public class Exam {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Exam name is required")
  private String name;

  private int time;

  @ManyToOne
  @JoinColumn(name = "creator_id")
  private User creator;

  @CreationTimestamp
  private Date createDate;

  @UpdateTimestamp
  private Date updateDate;
}
