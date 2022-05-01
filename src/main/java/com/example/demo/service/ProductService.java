package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Optional<Product> findEntity(int id) {
        return Optional.of(repository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    public List<ProductDTO> findAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    public int create(ProductDTO dto) {
       return Optional
               .of(mapper.convertToEntity(dto))
               .map(repository::save)
               .map(Product::getId)
               .get();

    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public ProductDTO update(int id, ProductDTO productDto) {
        return findEntity(id)
                .map(entity -> mapper.convertToEntity(productDto, entity))
                .map(repository::save)
                .map(mapper::convertToDto)
                .get();
    }

}