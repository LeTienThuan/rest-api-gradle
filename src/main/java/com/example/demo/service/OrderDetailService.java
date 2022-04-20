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
    private final OrderDetailMapper orderDetailMapper;

    public void deleteByOrderId(Orders order) {
        orderDetailRepository.deleteByOrderId(order);
    }

    public List<OrderDetail> getByOrderId(int orderId){
        return orderDetailRepository.findByOrdersId(orderId);
    }
    public void update(OrderDetail orderDetail){
        orderDetailRepository.save(orderDetail);
    }

    public void deleteById(int id){
        orderDetailRepository.deleteById(id);
    }
}
