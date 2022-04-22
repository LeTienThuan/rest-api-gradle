package com.example.demo.thymeleaf;


import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.Orders;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfTemplate {
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    public void readContentFromThymeleaf(String htmlContent) throws IOException {
        FileWriter fw = new FileWriter("src/main/resources/templates/invoice.html");
        fw.write(htmlContent);
        fw.flush();
        fw.close();
    }

    public double calculateTotal(List<OrderDetailDTO> orderDetailsDto){
        double total = 0;
        for(OrderDetailDTO dto : orderDetailsDto){
            total += dto.getTotal();
        }
        return total;
    }

    public ResponseEntity<byte[]> parseThymeleafTemplate(int id) throws IOException {
        List<OrderDetailDTO> orderDetailsDto = orderDetailMapper.convertToDto(orderDetailService.getByOrderId(id));
        List<String> productsName = new ArrayList<>();
        for (OrderDetailDTO orderDetailDto : orderDetailsDto) {
            String productName = productService.findEntity(orderDetailDto.getProductId()).get().getName();
            productsName.add(productName);
        }
        Orders order = orderService.findById(id);
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("items", orderDetailsDto);
        context.setVariable("productName", productsName);
        context.setVariable("customer", order.getCustomer());
        context.setVariable("orderId", id);
        context.setVariable("totalMoney", calculateTotal(orderDetailsDto));

        String result = templateEngine.process("thymeleaf_template", context);
        readContentFromThymeleaf(result);
        htmlToPdf("src/main/resources/templates/invoice.html", "src/main/resources/templates/invoice.pdf");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        File file = new File("src/main/resources/templates/invoice.pdf");
        byte[] content =  Files.readAllBytes(file.toPath());
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    private static Document html5ParseDocument(String inputHTML) throws IOException {
        org.jsoup.nodes.Document doc;
        doc = Jsoup.parse(new File(inputHTML), "UTF-8");
        return new W3CDom().fromJsoup(doc);
    }

    private static void htmlToPdf(String inputHTML, String outputPdf) throws IOException {
        Document doc = html5ParseDocument(inputHTML);
        String baseUri = FileSystems.getDefault()
                .getPath("src/main/resources/templates")
                .toUri()
                .toString();
        OutputStream os = new FileOutputStream(outputPdf);
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withUri(outputPdf);
        builder.toStream(os);
        builder.useFont(new File("src/main/resources/templates/BeVietnamPro-Light.ttf"), "myFont");
        builder.withW3cDocument(doc, baseUri);
        builder.run();
        os.close();
    }
}
