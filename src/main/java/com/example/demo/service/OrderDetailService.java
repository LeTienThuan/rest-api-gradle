package com.example.demo.service;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.repository.OrderDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailService {
    private final ProductService productService;
    private final OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getByOrderId(int orderId){

        return orderDetailRepository.findByOrdersId(orderId);
    }

    public double calculateTotalMoney(List<OrderDetailDTO> orderDetailsDto){
        double total = 0;
        for(OrderDetailDTO dto : orderDetailsDto){
            total += dto.getTotal();
        }
        return total;
    }

    public List<String> getListProductName(List<OrderDetailDTO> orderDetailsDto){
        List<String> productsName = new ArrayList<>();
        for (OrderDetailDTO orderDetailDto : orderDetailsDto) {
            String productName = productService.findEntity(orderDetailDto.getProductId()).get().getName();
            productsName.add(productName);
        }
        return productsName;
    }

    public void deleteById(int id){
        orderDetailRepository.deleteById(id);
    }
}
