package com.example.rag.Rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class pdfLoaderRag {

    @Value("classpath:documents/Eazybytes_HR_Policies.pdf")
    Resource hrPolicy;

    private final VectorStore vectorStore;
    public pdfLoaderRag(VectorStore vectorStore){
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void policyLoader(){
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(hrPolicy);
        List<Document> docs = tikaDocumentReader.get();
        TextSplitter textSplitter = TokenTextSplitter.builder().withChunkSize(200).withMaxNumChunks(400).build();
        vectorStore.add(textSplitter.split(docs));
    }
}
