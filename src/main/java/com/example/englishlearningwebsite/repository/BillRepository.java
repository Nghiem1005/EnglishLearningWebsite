package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Bill;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.models.ICourseSeller;
import com.example.englishlearningwebsite.models.IStatisticDay;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
  List<Bill> findBillsByUser(User stuednt);
  List<Bill> findBillsByCourse(Course course);
  @Query(value = "select course_id as courseId, count(course_id) as amount from tbl_bill group by course_id order by amount", nativeQuery = true)
  List<ICourseSeller> bestSeller();
  @Query(value = "select sum(total_price) as price from tbl_bill", nativeQuery = true)
  double totalPrice();

  @Query(value = "select weekday(b.create_date) as weekDay, SUM(b.total_price) as totalValue  from tbl_bill as b \n"
      + "inner join tbl_course as c on b.course_id = c.id\n"
      + "where c.course_type = :typeCourse \n"
      + "and b.create_date < now() and b.create_date > date_sub(now(), interval :amountDay Day)\n"
      + "group by weekday(create_date)", nativeQuery = true)
  List<IStatisticDay> findRevenueByDayAndTypeCourse(@Param("amountDay") int amountDay, @Param("typeCourse") String typeCourse);

  @Query(value = "select weekday(create_date) as weekDay, SUM(total_price) as totalValue  from tbl_bill \n"
      + "      where create_date < now() and create_date > date_sub(now(), interval :amountDay Day) \n"
      + "      group by weekday(create_date)", nativeQuery = true)
  List<IStatisticDay> findRevenueByDay(@Param("amountDay") int amountDay);
}
