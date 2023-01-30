package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.RoleRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.RoleResponseDTO;
import com.example.englishlearningwebsite.service.RoleService;
import com.example.englishlearningwebsite.utils.Utils;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/role")
public class RoleController {
  @Autowired
  private RoleService roleService;

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createRole(@RequestBody @Valid RoleRequestDTO roleRequestDTO) {
    return roleService.createRole(roleRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateRole(@RequestBody @Valid RoleRequestDTO roleRequestDTO,
      @RequestParam(name = "id") Long id) {
    return roleService.updateRole(roleRequestDTO, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteRole(@RequestParam(name = "id") Long id) {
    return roleService.deleteRole(id);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ResponseObject> getRoleById(@PathVariable(name = "id") Long id) {
    return roleService.getRoleById(id);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllRole(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return roleService.getAllRole(pageable);
  }
}
