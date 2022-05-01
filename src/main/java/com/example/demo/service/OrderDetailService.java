package com.example.demo.service;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.repository.OrderDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderDetailService {
    private final ProductService productService;
    private final OrderDetailRepository repository;

    public List<OrderDetail> getByOrderId(int orderId){
        return repository.findByOrdersId(orderId);
    }

    public double calculateTotalMoney(List<OrderDetailDTO> orderDetailsDto){
        return orderDetailsDto
                .stream()
                .map(OrderDetailDTO::getTotal)
                .reduce((double) 0, Double::sum);
    }

    public List<String> getListProductName(List<OrderDetailDTO> orderDetailsDto){
        return orderDetailsDto
                .stream()
                .map(OrderDetailDTO::getProductId)
                .map(productService::findEntity)
                .map(entity -> entity.get().getName())
                .collect(Collectors.toList());
    }
}
