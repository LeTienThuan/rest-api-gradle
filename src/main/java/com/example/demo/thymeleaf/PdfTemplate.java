package com.example.demo.thymeleaf;


import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.io.RandomAccess;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.PDFText2HTML;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfTemplate {
    @Autowired
    private  OrderDetailService orderDetailService;
    @Autowired
    private  OrderDetailMapper orderDetailMapper;

    @Autowired
    private ProductService productService;

    public String parseThymeleafTemplate() {
        List<OrderDetailDTO> orderDetailsDto = orderDetailMapper.convertToDto(orderDetailService.getByOrderId(9));
        List<String> productsName = new ArrayList<>();
        for(OrderDetailDTO orderDetailDto : orderDetailsDto){
            String productName = productService.findEntity(orderDetailDto.getProductId()).get().getName();
            productsName.add(productName);
        }
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("items", orderDetailsDto);
        context.setVariable("productName", productsName);
        System.out.println(productsName);

        return templateEngine.process("abc", context);
    }

    public static void main(String[] args) throws IOException {
//        File file = new File("src/main/resources/templates/test.html");
////        String text = new PdfTemplate().parseThymeleafTemplate();
//        PDDocument document = PDDocument.load(file);
//        PDPage page = new PDPage();
//        document.addPage(page);
//        document.save("zxc.pdf");
////        PDPageContentStream contentStream = new PDPageContentStream(document, page);
//        contentStream.beginText();
//        contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
//        contentStream.newLineAtOffset(25, 500);
//        contentStream.showText("<html>");
//        contentStream.endText();
//        contentStream.close();
//        document.save("tester.pdf");
//        document.close();
    }
}
