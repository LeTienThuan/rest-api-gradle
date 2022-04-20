package com.example.demo.mapper;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "customer", expression = "java(customerMapper.convertToDto(order.getCustomer()))")
    @Mapping(target = "orderDetail", expression = "java(orderDetailMapper.convertToDto(order.getOrderDetail()))")
    OrderDTO convertToDto(Orders order, CustomerMapper customerMapper, OrderDetailMapper orderDetailMapper);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", expression = "java(customerMapper.convertToEntityBelongToOrder(orderDto.getCustomer()))")
    @Mapping(target = "orderDetail", expression = "java(orderDetailMapper.convertToEntity(orderDto.getOrderDetail(), order, orderDetailMapper))")
    Orders convertToEntity(OrderDTO orderDto, @MappingTarget Orders order, CustomerMapper customerMapper, OrderDetailMapper orderDetailMapper);

    default List<OrderDTO> convertToDto(List<Orders> orders, CustomerMapper customerMapper, OrderMapper orderMapper, OrderDetailMapper orderDetailMapper) {
        List<OrderDTO> ordersDto = new ArrayList<>();
        for (Orders order : orders) {
            ordersDto.add(orderMapper.convertToDto(order, customerMapper, orderDetailMapper));
        }
        return ordersDto;
    }
}
