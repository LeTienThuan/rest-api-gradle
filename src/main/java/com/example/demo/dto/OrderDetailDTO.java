package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private int id;
    private int productId;
    private int quantity;
    private double price;
    private double total;
}
