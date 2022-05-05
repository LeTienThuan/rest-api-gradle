package com.example.demo.util;
import com.example.demo.constant.Pdf;
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

@Service
@AllArgsConstructor
public class  PdfGenerator {

    public void readContentFromThymeleaf(String htmlContent, String path) throws IOException {
        FileWriter fw = new FileWriter(path);
        fw.write(htmlContent);
        fw.flush();
        fw.close();
    }

    public String parseThymeleafTemplate(Context context){
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(Pdf.THYMELEAF_INVOICE , context);
    }

    private Document html5ParseDocument(String htmlPath) throws IOException {
        org.jsoup.nodes.Document doc;
        doc = Jsoup.parse(new File(htmlPath), "UTF-8");
        return new W3CDom().fromJsoup(doc);
    }

    public void htmlToPdf(String fontPath, String pdfPath, String htmlPath ) throws IOException {
        Document doc = html5ParseDocument(htmlPath);
        String baseUri = FileSystems.getDefault()
                .getPath("src/main/resources/templates")
                .toUri()
                .toString();
        OutputStream os = new FileOutputStream(pdfPath);

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withUri(pdfPath);
        builder.toStream(os);
        builder.useFont(new File(fontPath), "myFont");
        builder.withW3cDocument(doc, baseUri);
        builder.run();
        os.close();
    }
}
