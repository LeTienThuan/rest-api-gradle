package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.OrderMapper;
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
    private final OrderDetailService orderDetailService;
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
    public void removeUnuseOrderDetail(int orderId,List<OrderDetailDTO> orderDetailsDto){
        List<Integer> listId = getListIdOrderDetailFromRequest(orderDetailsDto);
        List<OrderDetail> entities = orderDetailService.getByOrderId(orderId);

        for(OrderDetail entity : entities){
            if(!listId.contains(entity.getId())){
                orderDetailService.deleteById(entity.getId());
           }
        }
    }

    public Optional<Orders> findEntity(int id) {
        return Optional.of(orderRepository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    public OrderDTO update(int id, OrderDTO orderDto) {
            Orders order = findEntity(id).get();
            Orders updatedOrder = orderMapper.convertToEntity(orderDto, order);
            removeUnuseOrderDetail(id, orderDto.getOrderDetail());
            orderRepository.save(updatedOrder);
            return orderMapper.convertToDto(updatedOrder);
    }

    public List<OrderDTO> findAll() {
        List<Orders> orders = orderRepository.findAll();
        return orderMapper.convertToDto(orders);
    }

    public Orders findById(int id){
        return findEntity(id).get();
    }
}
