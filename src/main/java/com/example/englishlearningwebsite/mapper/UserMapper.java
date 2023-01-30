package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.request.UserRequestDTO;
import com.example.englishlearningwebsite.dto.response.UserResponseDTO;
import com.example.englishlearningwebsite.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "email", source = "dto.email")
  @Mapping(target = "phone", source = "dto.phone")
  @Mapping(target = "enable", source = "dto.enable")
  @Mapping(target = "role.id", source = "dto.role")
  @Mapping(target = "password", source = "dto.password")
  @Mapping(target = "images", expression = "java(null)")
  User userRequestDTOtoUser(UserRequestDTO dto);

  @Mapping(target = "id", source = "user.id")
  @Mapping(target = "name", source = "user.name")
  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "phone", source = "user.phone")
  @Mapping(target = "enable", source = "user.enable")
  @Mapping(target = "image", source = "user.images")
  @Mapping(target = "role", source = "user.role")
  @Mapping(target = "provider", source = "user.provider")
  UserResponseDTO userToUserResponseDTO(User user);
}
