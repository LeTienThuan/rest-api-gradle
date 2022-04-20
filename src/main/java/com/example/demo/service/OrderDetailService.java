package com.example.demo.service;

import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.repository.OrderDetailRepository;
import lombok.AllArgsConstructor;
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
