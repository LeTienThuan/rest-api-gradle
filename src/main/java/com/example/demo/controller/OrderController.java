package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.service.OrderService;
import com.example.demo.util.PdfGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/order")
public class OrderController {
    private final OrderService orderService;
    private final PdfGenerator pdfTemplate;

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
    public OrderDTO update(@PathVariable(name = "id") int id, @RequestBody OrderDTO orderDto) {
        return orderService.update(id, orderDto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int id) {
        orderService.delete(id);
    }

    @GetMapping(path = "/invoice/{id}")
    public ResponseEntity<byte[]> getHTML(@PathVariable(name = "id") int id) throws IOException {
        return pdfTemplate.getInvoicePDF(id);
    }
}

