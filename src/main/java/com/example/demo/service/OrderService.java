package com.example.demo.service;

import com.example.demo.constant.Pdf;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.Orders;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.util.PdfGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderMapper orderMapper;
    private final OrderDetailRepository orderDetailRepository;

    private PdfGenerator pdfGenerator;

    public int create() {
        return orderRepository.save(new Orders()).getId();
    }

    public void delete(int id) {
        orderRepository.deleteById(id);
    }

    public List<Integer> getListOrderDetailId(List<OrderDetailDTO> orderDetailsDto) {
        return orderDetailsDto.stream()
                .filter(orderDetail -> orderDetail.getId() != 0)
                .map(OrderDetailDTO::getId)
                .collect(Collectors.toList());
    }

    public void removeUnuseOrderDetail(int orderId, List<OrderDetailDTO> orderDetailsDto) {
        List<Integer> orderDetailIds = getListOrderDetailId(orderDetailsDto);
        orderDetailService
                .getByOrderId(orderId)
                .stream()
                .filter(orderDetail -> !orderDetailIds.contains(orderDetail.getId()))
                .forEach(orderDetailRepository::delete);
    }

    public Optional<Orders> findEntity(int id) {
        return Optional.of(orderRepository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    public OrderDTO update(int id, OrderDTO orderDto) {
        removeUnuseOrderDetail(id, orderDto.getOrderDetail());
        return findEntity(id)
                .map(entity -> orderMapper.updateEntity(orderDto, entity))
                .map(orderRepository::save)
                .map(orderMapper::convertToDto)
                .get();
    }

    public List<OrderDTO> findAll() {
        return Optional.of(orderRepository.findAll())
                .map(orderMapper::convertToDto)
                .get();
    }

    public Context setVariablesInvoiceThymeleafTemplate(int orderId){
        List<OrderDetailDTO> orderDetailsDto = orderDetailMapper.convertToDto(orderDetailService.getByOrderId(orderId));
        List<String> productsName = orderDetailService.getListProductName(orderDetailsDto);
        Orders order = findEntity(orderId).get();

        Context context = new Context();
        context.setVariable("items", orderDetailsDto);
        context.setVariable( "productName", productsName);
        context.setVariable("customer", order.getCustomer());
        context.setVariable("orderId", orderId);
        context.setVariable("totalMoney", orderDetailService.calculateTotalMoney(orderDetailsDto));
        return context;
    }

    public ResponseEntity<byte[]> getInvoicePDF(int id) throws IOException {
        Context context = setVariablesInvoiceThymeleafTemplate(id);
        String htmlContent= pdfGenerator.parseThymeleafTemplate(context);
        pdfGenerator.readContentFromThymeleaf(htmlContent, Pdf.INVOICE_HTML_GENERATED);
        pdfGenerator.htmlToPdf(Pdf.FONT_VIETNAM_PATH, Pdf.INVOICE_PDF_PATH,Pdf.INVOICE_HTML_GENERATED);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        File file = new File(Pdf.INVOICE_PDF_PATH);
        byte[] content =  Files.readAllBytes(file.toPath());
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }



}
