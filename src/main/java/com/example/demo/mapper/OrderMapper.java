package com.example.demo.mapper;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    @Autowired
    protected CustomerMapper customerMapper;
    @Autowired
    protected OrderDetailMapper orderDetailMapper;

    @Mapping(target = "customer", expression = "java(customerMapper.toDto(order.getCustomer()))")
    @Mapping(target = "orderDetail", expression = "java(orderDetailMapper.toDto(order.getOrderDetail()))")
    public abstract OrderDTO toDto(Orders order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", expression = "java(customerMapper.toEntity(orderDto.getCustomer()))")
    @Mapping(target = "orderDetail", expression = "java(orderDetailMapper.toEntity(orderDto.getOrderDetail(), order))")
    public abstract Orders updateEntity(OrderDTO orderDto, @MappingTarget Orders order);

    public abstract List<OrderDTO> toDto(List<Orders> orders);
}
