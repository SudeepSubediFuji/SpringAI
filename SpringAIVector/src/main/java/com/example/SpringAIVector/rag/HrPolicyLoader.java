package com.example.SpringAIVector.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.ai.reader.tika.TikaDocumentReader;

import java.util.List;
import java.util.logging.Logger;


@Component
public class HrPolicyLoader {

    private final VectorStore vectorStore;
    Logger logger = Logger.getLogger(HrPolicyLoader.class.getName());
    public HrPolicyLoader(VectorStore vectorStore){
        this.vectorStore=vectorStore;
    }

    @Value("classpath:/Documents/CompanyInfo.pdf")
    Resource CompanyInfo;

    @PostConstruct
    public void pdfLoader(){
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(CompanyInfo);
        List<Document> companyInfo = tikaDocumentReader.get();
        vectorStore.add(companyInfo);
        logger.info("CompanyInfo got loaded to VectorStore");
    }

}
