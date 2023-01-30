package com.example.englishlearningwebsite.repository;

import com.example.englishlearningwebsite.entities.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findRoleByName(String name);
}
