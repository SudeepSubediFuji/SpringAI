package com.fuji.Evaluator.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class DataLoader {
    @Value("classpath:/Documents/Japan.pdf")
    Resource GroundTruth;
    List<Document> doc;

    @PostConstruct
    public void pdfLoader(){
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(GroundTruth);
         this.doc = tikaDocumentReader.get();
         System.out.println("Data loaded successfully!");
    }

    public List<Document> getJapanDoc(){
        return doc;
    }
}
