package com.example.demo.service;

import com.example.demo.entity.OrderDetail;
import com.example.demo.repository.OrderDetailRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getByOrderId(int orderId){
        return orderDetailRepository.findByOrdersId(orderId);
    }
    public void deleteById(int id){
        orderDetailRepository.deleteById(id);
    }
}
