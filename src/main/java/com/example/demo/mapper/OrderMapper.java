package com.example.demo.mapper;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    @Autowired
    protected CustomerMapper customerMapper;
    @Autowired
    protected OrderDetailMapper orderDetailMapper;

    @Mapping(target = "customer", expression = "java(customerMapper.convertToDto(order.getCustomer()))")
    @Mapping(target = "orderDetail", expression = "java(orderDetailMapper.convertToDto(order.getOrderDetail()))")
    public abstract OrderDTO convertToDto(Orders order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", expression = "java(customerMapper.convertToEntityBelongToOrder(orderDto.getCustomer()))")
    @Mapping(target = "orderDetail", expression = "java(orderDetailMapper.convertToEntity(orderDto.getOrderDetail(), order))")
    public abstract Orders convertToEntity(OrderDTO orderDto, @MappingTarget Orders order);

    public abstract List<OrderDTO> convertToDto(List<Orders> orders);
}
