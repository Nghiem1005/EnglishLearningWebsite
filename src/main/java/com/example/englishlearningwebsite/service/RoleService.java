package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.RoleRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.RoleResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RoleService {
  ResponseEntity<ResponseObject> createRole(RoleRequestDTO roleRequestDTO);

  ResponseEntity<ResponseObject> updateRole(RoleRequestDTO roleRequestDTO, Long id);

  ResponseEntity<ResponseObject> deleteRole(Long id);

  ResponseEntity<ResponseObject> getRoleById(Long id);

  ResponseEntity<?> getAllRole(Pageable pageable);
}
