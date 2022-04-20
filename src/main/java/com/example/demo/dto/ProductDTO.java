package com.example.demo.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private String trademark;
    private double price;
    private String description;
}
