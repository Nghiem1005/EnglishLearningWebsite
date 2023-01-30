package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.request.BillRequestDTO;
import com.example.englishlearningwebsite.dto.response.BillResponseDTO;
import com.example.englishlearningwebsite.entities.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BillMapper {
  BillMapper INSTANCE = Mappers.getMapper( BillMapper.class );
  @Mapping(target = "billId", source = "b.id")
  @Mapping(target = "totalPrice", source = "b.totalPrice")
  @Mapping(target = "paymentMethod", source = "b.paymentMethod")
  @Mapping(target = "payDate", source = "b.payDate")
  @Mapping(target = "courseId", source = "b.course.id")
  @Mapping(target = "studentId", source = "b.user.id")
  @Mapping(target = "studentName", source = "b.user.name")
  BillResponseDTO billToBillResponseDTO(Bill b);
}
