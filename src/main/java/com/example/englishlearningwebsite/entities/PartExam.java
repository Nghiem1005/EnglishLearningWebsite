package com.example.englishlearningwebsite.entities;

import com.example.englishlearningwebsite.entities.enums.PartExamType;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_part_exam")
public class PartExam {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Part exam content is required")
  private String name;

  @Enumerated(EnumType.STRING)
  private PartExamType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id")
  private User creator;

  @CreationTimestamp
  private Date createDate;

  @UpdateTimestamp
  private Date updateDate;

  @OneToMany(mappedBy="partExam", orphanRemoval=true, cascade={CascadeType.REMOVE}, fetch = FetchType.EAGER)
  private List<Question> questions;
}
