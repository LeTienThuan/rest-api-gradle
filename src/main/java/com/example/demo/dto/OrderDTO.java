package com.example.demo.dto;

import lombok.Data;

import java.util.List;


@Data
public class OrderDTO {
    private int id;
    private CustomerDTO customer;
    private String deliveryAddress;
    private List<OrderDetailDTO> orderDetail;
}
