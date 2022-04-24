package com.example.demo.exportfile;


import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.Orders;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderService;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.dom.Document;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

@Service
@AllArgsConstructor
public class Pdf {
    public static final String INVOICE_PDF_PATH = "src/main/resources/templates/invoice.pdf";
    public static final String FONT_PATH = "src/main/resources/templates/BeVietnamPro-Light.ttf";
    public static final String THYMELEAF_GENERATED = "src/main/resources/templates/invoice.html";
    private OrderDetailService orderDetailService;
    private OrderDetailMapper orderDetailMapper;
    private OrderService orderService;

    public void readContentFromThymeleaf(String htmlContent) throws IOException {
        FileWriter fw = new FileWriter(THYMELEAF_GENERATED);
        fw.write(htmlContent);
        fw.flush();
        fw.close();
    }

    public Context setVariablesThymeleaf(int orderId){
        List<OrderDetailDTO> orderDetailsDto = orderDetailMapper.convertToDto(orderDetailService.getByOrderId(orderId));
        List<String> productsName = orderDetailService.getListProductName(orderDetailsDto);
        Orders order = orderService.findById(orderId);

        Context context = new Context();
        context.setVariable("items", orderDetailsDto);
        context.setVariable( "productName", productsName);
        context.setVariable("customer", order.getCustomer());
        context.setVariable("orderId", orderId);
        context.setVariable("totalMoney", orderDetailService.calculateTotalMoney(orderDetailsDto));
        return context;
    }

    public String parseThymeleafTemplate(int orderId){
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = setVariablesThymeleaf(orderId);
        return templateEngine.process("thymeleaf_template", context);
    }

    private Document html5ParseDocument() throws IOException {
        org.jsoup.nodes.Document doc;
        doc = Jsoup.parse(new File(THYMELEAF_GENERATED), "UTF-8");
        return new W3CDom().fromJsoup(doc);
    }

    private void htmlToPdf() throws IOException {
        Document doc = html5ParseDocument();
        String baseUri = FileSystems.getDefault()
                .getPath("src/main/resources/templates")
                .toUri()
                .toString();
        OutputStream os = new FileOutputStream(INVOICE_PDF_PATH);

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withUri(INVOICE_PDF_PATH);
        builder.toStream(os);
        builder.useFont(new File(FONT_PATH), "myFont");
        builder.withW3cDocument(doc, baseUri);
        builder.run();
        os.close();
    }

    public ResponseEntity<byte[]> getInvoicePDF(int id) throws IOException {
        String htmlContent= parseThymeleafTemplate(id);
        readContentFromThymeleaf(htmlContent);
        htmlToPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        File file = new File(INVOICE_PDF_PATH);
        byte[] content =  Files.readAllBytes(file.toPath());
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
