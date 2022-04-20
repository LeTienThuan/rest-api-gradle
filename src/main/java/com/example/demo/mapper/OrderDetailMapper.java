package com.example.demo.mapper;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    OrderDetailDTO convertToDto(OrderDetail orderDetail);

    List<OrderDetailDTO> convertToDto(List<OrderDetail> orderDetails);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", source = "order")
    OrderDetail convertToEntity(OrderDetailDTO orderDetailDto, Orders order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    OrderDetail convertToEntity(@MappingTarget OrderDetail orderDetail, OrderDetailDTO orderDetailDto);

    default OrderDetail convertToEntityWithId(OrderDetailDTO dto, Orders order){
        OrderDetail entity = new OrderDetail();
        entity.setOrders(order);
        entity.setId(dto.getId());
        entity.setPrice(dto.getPrice());
        entity.setProductId(dto.getProductId());
        entity.setQuantity(dto.getQuantity());
        entity.setTotal(dto.getTotal());
        return entity;
    }

    default List<OrderDetail> convertToEntity(List<OrderDetailDTO> orderDetailDto, Orders order, OrderDetailMapper orderDetailMapper) {
        List<OrderDetail> list = new ArrayList<>();
        for (OrderDetailDTO orderDetailDTO : orderDetailDto) {
            list.add(orderDetailMapper.convertToEntityWithId(orderDetailDTO, order));
        }
        return list;
    }


}
