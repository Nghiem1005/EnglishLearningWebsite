package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.Schedule;
import com.google.api.client.util.DateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  Optional<Schedule> findScheduleByLesson(Lesson lesson);
  Optional<Schedule> findScheduleByLessonAndStartDate(Lesson lesson, Date dateTime);
  Optional<Schedule> findScheduleByLessonAndStartDateBetween(Lesson lesson, DateTime startDate,
      Date endDate);
  @Query(value = "select s from Schedule s where ((:startDate between s.startDate and s.endDate) or (:endDate between s.startDate and s.endDate) or (:startDate < s.startDate and :endDate > s.endDate)) and s.lesson = :lesson")
  List<Schedule> findScheduleByLessonAndStartDateAndEndDate(@Param("lesson") Lesson lesson, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
