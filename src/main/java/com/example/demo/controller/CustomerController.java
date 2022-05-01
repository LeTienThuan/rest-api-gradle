package com.example.demo.controller;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int create(@RequestBody CustomerDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<CustomerDTO> findAll() {
        return service.findAll();
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int id){
        service.deleteById(id);
    }

    @PutMapping(path = "/{id}")
    public CustomerDTO update(@PathVariable(name = "id") int id, @RequestBody CustomerDTO customerDto){
        return service.update(id, customerDto);
    }
}
