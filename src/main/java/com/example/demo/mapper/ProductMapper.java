package com.example.demo.mapper;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDto(Product product);

    @Mapping(target = "id", ignore = true)
    Product toEntityIgnoreId(ProductDTO productDto);

    @Mapping(target = "id", ignore = true)
    Product toEntityIgnoreId(ProductDTO productDto, @MappingTarget Product product);
}
