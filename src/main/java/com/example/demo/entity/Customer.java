package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;
    private int age;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String address;
}

