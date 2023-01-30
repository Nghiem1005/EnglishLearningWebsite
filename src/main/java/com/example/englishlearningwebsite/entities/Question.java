package com.example.englishlearningwebsite.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_question")
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String content;

  private int serial;

  private String document;

  private String type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "part_exam_id")
  private PartExam partExam;

  @OneToMany(mappedBy="question", orphanRemoval=true, cascade={CascadeType.REMOVE}, fetch = FetchType.EAGER)
  private List<Answer> answers;
}
