package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
  List<Course> findCoursesByTeacher(User teacher);
  Page<Course> findCoursesByTeacher(Pageable pageable, User teacher);
  Page<Course> findCoursesByCourseType(Pageable pageable, String courseType);
  Page<Course> findCoursesByTeacherIsNull(Pageable pageable);

}
