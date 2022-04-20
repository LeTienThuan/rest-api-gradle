package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Integer> getListIdOrderDetailFromRequest(List<OrderDetailDTO> orderDetailsDto){
        List<Integer> listId = new ArrayList<>();
        for(OrderDetailDTO dto : orderDetailsDto) {
            if (dto.getId() != 0) {
                listId.add(dto.getId());
            }
        }
        return listId;
    }
    public void deleteUnuseOrderDetail(int orderId,List<OrderDetailDTO> orderDetailsDto){
        List<Integer> listId = getListIdOrderDetailFromRequest(orderDetailsDto);
        List<OrderDetail> entities = orderDetailService.getByOrderId(orderId);

        for(OrderDetail entity : entities){
            if(!listId.contains(entity.getId())){
                orderDetailService.deleteById(entity.getId());
           }
        }
    }

    public OrderDTO update(int id, OrderDTO orderDto) {
        Optional<Orders> unknownOrder = orderRepository.findById(id);
        if (unknownOrder.isPresent()) {
            Orders order = unknownOrder.get();

            deleteUnuseOrderDetail(id, orderDto.getOrderDetail());
            order.setOrderDetail(orderDetailMapper.convertToEntity(orderDto.getOrderDetail(), order, orderDetailMapper));
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
