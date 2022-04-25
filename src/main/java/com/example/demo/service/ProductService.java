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

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Optional<Product> findEntity(int id) {
        return Optional.of(productRepository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    public List<ProductDTO> findAll() {
        return productMapper.convertToDto(productRepository.findAll());
    }

    public int create(ProductDTO productDto) {
        Product product = productMapper.convertToEntity(productDto);
        return productRepository.save(product).getId();
    }

    public void deleteById(int id) {
        productRepository.deleteById(id);
    }

    public ProductDTO update(int id, ProductDTO productDto) {
        return findEntity(id)
                .map(entity -> productMapper.convertToEntity(productDto, entity))
                .map(productRepository::save)
                .map(productMapper::convertToDto).get();
    }

}