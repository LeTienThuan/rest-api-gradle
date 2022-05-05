package com.example.demo.mapper;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO toDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    Customer toEntityIgnoreId(CustomerDTO customerDTO);

    Customer toEntity(CustomerDTO customerDto);

    @Mapping(target = "id", ignore = true)
    Customer toEntityIgnoreId(@MappingTarget Customer customer, CustomerDTO customerDto);


}
