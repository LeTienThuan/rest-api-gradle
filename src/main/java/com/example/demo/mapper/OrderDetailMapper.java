package com.example.demo.mapper;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    OrderDetailDTO toDto(OrderDetail orderDetail);

    List<OrderDetailDTO> toDto(List<OrderDetail> orderDetails);

    @Mapping(source = "dto.id", target = "id")
    @Mapping(target = "orders", source = "order")
    OrderDetail toEntity(OrderDetailDTO dto, Orders order);

    default List<OrderDetail> toEntity(List<OrderDetailDTO> orderDetailDto, Orders order) {
        List<OrderDetail> list = new ArrayList<>();
        for (OrderDetailDTO orderDetailDTO : orderDetailDto) {
            list.add(toEntity(orderDetailDTO, order));
        }
        return list;
    }
}
