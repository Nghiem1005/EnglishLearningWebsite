package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.StudentCourse;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.models.IStatisticDay;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
  List<StudentCourse> findStudentCoursesByUser(User user);
  List<StudentCourse> findStudentCoursesByCourse(Course course);
  Page<StudentCourse> findStudentCoursesByUser(Pageable pageable, User user);
  Page<StudentCourse> findStudentCoursesByCourse(Pageable pageable, Course course);
  Optional<StudentCourse> findStudentCourseByCourseAndUser(Course course, User user);

  /*@Query(value = "select weekday(create_date) as weekDay, count(id) as totalValue  from tbl_user "
      + "where create_date <= current_date() and create_date > date_sub(current_date(), interval :amountDay Day) and "
      + "group by weekday(create_date)", nativeQuery = true)*/
  @Query(value = "select weekday(sc.create_date) as weekDay, count(sc.user_id) as totalValue from tbl_student_course as sc \n"
      + "inner join tbl_course as c on sc.course_id  = c.id\n"
      + "where sc.create_date <= now() and sc.create_date > date_sub(now(), interval :amountDay Day)\n"
      + "and c.course_type = :typeCourse \n"
      + "group by weekday(sc.create_date)", nativeQuery = true)
  List<IStatisticDay> findAllStudentTypeByDay(@Param("amountDay") int amountDay, @Param("typeCourse") String typeCourse);
}
