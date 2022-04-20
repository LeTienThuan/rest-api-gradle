package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> findAll() {
        return orderService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int create() {
        return orderService.create();
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public OrderDTO
    update(@PathVariable(name = "id") int id, @RequestBody OrderDTO orderDto) {
        return orderService.update(id, orderDto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int id) {
        orderService.delete(id);
    }

    @DeleteMapping("/find/{id}")
    public void deleteById(@PathVariable(name = "id") int id){orderDetailService.deleteById(id);}

}

