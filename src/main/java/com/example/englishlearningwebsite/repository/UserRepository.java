package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Role;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.models.IStatisticDay;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByEmail(String email);
  Optional<User> findUserByPhone(String phone);
  Optional<User> findUserByVerificationCode(String verifyCode);
  List<User> findUsersByRole(Role role);
  Optional<User> findUserByName(String name);
  Boolean existsByEmail(String email);
  Page<User> findUsersByRole(Role role, Pageable pageable);

  @Query(value = "select weekday(create_date) as weekDay, count(id) as totalValue  from tbl_user "
      + "where create_date <= current_date() and create_date > date_sub(current_date(), interval :amountDay Day) "
      + "group by weekday(create_date)", nativeQuery = true)
  List<IStatisticDay> findAllNewMemberByDay(@Param("amountDay") int amountDay);

  @Query(value = "SELECT u.id, u.email, u.enable, u.images, u.name, u.phone, u.role_id, u.provider "
      + "FROM tbl_user as u LEFT JOIN tbl_student_course as sc ON u.id = sc.user_id "
      + "where sc.course_id is null", nativeQuery = true)
  List<User> findUsersOutCourse();
}
