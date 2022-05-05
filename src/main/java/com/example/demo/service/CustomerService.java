package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public int create(CustomerDTO dto) {
        return Optional
                .of(mapper.toEntityIgnoreId(dto))
                .map(repository::save)
                .map(Customer::getId)
                .get();
    }

    public List<CustomerDTO> findAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<Customer> findEntity(int id) {
        return Optional
                .of(repository.findById(id)
                        .orElseThrow(NotFoundException::new));
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public CustomerDTO update(int id, CustomerDTO customerDto) {
        return findEntity(id)
                .map(entity -> mapper.toEntityIgnoreId(entity, customerDto))
                .map(repository::save)
                .map(mapper::toDto)
                .orElseThrow();
    }
}
