package com.example.englishlearningwebsite.entities.keys;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseKey implements Serializable {
  private Long user;
  private Long course;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StudentCourseKey that = (StudentCourseKey) o;
    return Objects.equals(user, that.user) && Objects.equals(course, that.course);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, course);
  }
}
