package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerMapper customerMapper;
    private final OrderDetailService orderDetailService;
    private final OrderDetailMapper orderDetailMapper;


    private final OrderMapper orderMapper;

    public int create() {
        return orderRepository.save(new Orders()).getId();
    }

    public void delete(int id) {
        orderRepository.deleteById(id);
    }

    public void updateOrderDetails(int id, OrderDTO orderDto, Orders order) {
        List<OrderDetail> orderDetails = orderDetailService.getByOrderId(id);
        List<OrderDetailDTO> orderDetailsDto = orderDto.getOrderDetail();

        int sizeOrderDetails = orderDetails.size();
        int sizeOrderDetailsDto = orderDetailsDto.size();

        if (sizeOrderDetails > sizeOrderDetailsDto) {
            for (int i = sizeOrderDetailsDto; i < sizeOrderDetails; i++) {
                orderDetailService.deleteById(orderDetails.get(i).getId());
            }
        }
        for (int i = 0; i < sizeOrderDetailsDto; i++) {
            boolean isInRangeOrderDetailSize = i + 1 <= sizeOrderDetails;
            OrderDetail orderDetail;

            if (isInRangeOrderDetailSize) {
                orderDetail = orderDetailMapper.convertToEntity(orderDetails.get(i), orderDetailsDto.get(i));
            } else {
                orderDetail = orderDetailMapper.convertToEntity(orderDetailsDto.get(i), order);
            }
            orderDetailService.update(orderDetail);
        }
    }

    public OrderDTO update(int id, OrderDTO orderDto) {
        Optional<Orders> unknownOrder = orderRepository.findById(id);
        if (unknownOrder.isPresent()) {
            Orders order = unknownOrder.get();
            updateOrderDetails(id, orderDto, order);
            order.setDeliveryAddress(orderDto.getDeliveryAddress());
            order.setCustomer(customerMapper.convertToEntityBelongToOrder(orderDto.getCustomer()));
            orderRepository.save(order);

            return orderMapper.convertToDto(order, customerMapper, orderDetailMapper);
        } else {
            return null;
        }
    }

    public List<OrderDTO> findAll() {
        List<Orders> orders = orderRepository.findAll();
        return orderMapper.convertToDto(orders, customerMapper, orderMapper, orderDetailMapper);
    }
}
