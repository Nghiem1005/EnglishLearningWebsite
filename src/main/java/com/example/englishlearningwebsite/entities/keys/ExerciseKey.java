package com.example.englishlearningwebsite.entities.keys;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseKey implements Serializable {
  private Long exam;
  private Long lesson;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExerciseKey that = (ExerciseKey) o;
    return Objects.equals(exam, that.exam) && Objects.equals(lesson, that.lesson);
  }

  @Override
  public int hashCode() {
    return Objects.hash(exam, lesson);
  }
}
