package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.request.RoleRequestDTO;
import com.example.englishlearningwebsite.dto.response.RoleResponseDTO;
import com.example.englishlearningwebsite.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  RoleMapper INSTANCE = Mappers.getMapper( RoleMapper.class );
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "description", source = "dto.description")
  Role roleRequestDTOtoRole(RoleRequestDTO dto);

  @Mapping(target = "id", source = "role.id")
  @Mapping(target = "name", source = "role.name")
  @Mapping(target = "description", source = "role.description")
  RoleResponseDTO roleToRoleResponseDTO(Role role);
}
