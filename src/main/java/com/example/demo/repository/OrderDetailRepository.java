package com.example.demo.repository;

import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Modifying
    @Transactional
    @Query("delete from OrderDetail od where od.orders.id = :#{#order.id}")
    void deleteByOrderId(@Param("order") Orders order);

    List<OrderDetail> findByOrdersId(int orderId);
}
