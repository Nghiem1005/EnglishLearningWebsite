package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.RoleRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.RoleResponseDTO;
import com.example.englishlearningwebsite.entities.Role;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.exception.ResourceAlreadyExistsException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.RoleMapper;
import com.example.englishlearningwebsite.repository.RoleRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.RoleService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
  //private final RoleMapper mapper = Mappers.getMapper(RoleMapper.class);

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;
  @Override
  public ResponseEntity<ResponseObject> createRole(RoleRequestDTO roleRequestDTO) {
    Role role = RoleMapper.INSTANCE.roleRequestDTOtoRole(roleRequestDTO);
    role = checkExists(role);

    Role roleSaved = roleRepository.save(role);
    RoleResponseDTO roleResponseDTO = RoleMapper.INSTANCE.roleToRoleResponseDTO(roleSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create role successfully!", roleResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateRole(RoleRequestDTO roleRequestDTO, Long id) {
    Role role = RoleMapper.INSTANCE.roleRequestDTOtoRole(roleRequestDTO);
    Role getRole = roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find role with ID = " + id));
    role.setId(id);
    role = checkExists(role);

    Role roleSaved = roleRepository.save(role);
    RoleResponseDTO roleResponseDTO = RoleMapper.INSTANCE.roleToRoleResponseDTO(roleSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update role successfully!", roleResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteRole(Long id) {
    Role role = roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find role with ID = " + id));

    //Delete user by role
    List<User> userList = userRepository.findUsersByRole(role);
    for (User user : userList){
      user.setRole(null);
      userRepository.save(user);
    }
    roleRepository.delete(role);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete role successfully!"));
  }

  @Override
  public ResponseEntity<ResponseObject> getRoleById(Long id) {
    Role role = roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find role with ID = " + id));
    RoleResponseDTO roleResponseDTO = RoleMapper.INSTANCE.roleToRoleResponseDTO(role);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Success", roleResponseDTO));
  }

  @Override
  public ResponseEntity<?> getAllRole(Pageable pageable) {
    Page<Role> getRoleList = roleRepository.findAll(pageable);
    List<Role> roleList = getRoleList.getContent();

    //Convert roll to RoleResponseDTO
    List<RoleResponseDTO> roleResponseDTOList = new ArrayList<>();
    for (Role role : roleList) {
      RoleResponseDTO roleResponseDTO = RoleMapper.INSTANCE.roleToRoleResponseDTO(role);
      roleResponseDTOList.add(roleResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Success", roleResponseDTOList));
  }

  private Role checkExists(Role role) {
    // Check role name exists
    Optional<Role> getRole = roleRepository.findRoleByName(role.getName());
    if (getRole.isPresent()) {
      if (role.getId() == null) {
        throw new ResourceAlreadyExistsException("Role name already exists");
      } else {
        if (role.getId() != getRole.get().getId()) {
          throw new ResourceAlreadyExistsException("Role name already exists");
        }
      }
    }
    return role;
  }
}
