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

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public int create(CustomerDTO dto) {
        return customerRepository.save(customerMapper.convertToEntity(dto)).getId();
    }

    public List<CustomerDTO> findAll() {
        return customerMapper.convertToDto(customerRepository.findAll());
    }

    public Optional<Customer> findEntity(int id) {
        return Optional.of(customerRepository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    public void deleteById(int id) {
        customerRepository.deleteById(id);
    }

    public CustomerDTO update(int id, CustomerDTO customerDto) {
        return findEntity(id)
                .map(entity -> customerMapper.convertToEntity(entity, customerDto))
                .map(customerRepository::save)
                .map(customerMapper::convertToDto).get();
    }
}
