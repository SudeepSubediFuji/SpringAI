### SpringAiVector
本プロジェクトではファイルを読み読んでベクタデータベースに保存されています。

1. systemPrompt → promptTemplate/...
2. userPrompt → ユーザー側のメッセージ
3. コンテクスト　→　Documents/CompanyInfo.pdf : ベクタデータベースに保存

ベクタデータストアに書類を読み込み処理について、説明はragプロジェクトに書いております（本プロジェクトとRAGプロジェクトと）。
参照資料：https://github.com/SudeepSubediFuji/SpringAI/tree/main/rag

以下は普通のRAGの実装です：
ファイルローダ：
```java
@Component
public class HrPolicyLoader {

    private final VectorStore vectorStore;
    Logger logger = Logger.getLogger(HrPolicyLoader.class.getName());
    public HrPolicyLoader(VectorStore vectorStore){
        this.vectorStore=vectorStore;
    }
    @Value("classpath:/Documents/CompanyInfo.pdf")
    Resource CompanyInfo;

    //プロジェクトとをコンパイルした時を実行
    @PostConstruct
    public void pdfLoader(){
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(CompanyInfo);
        List<Document> companyInfo = tikaDocumentReader.get();
        TextSplitter textSplitter = TokenTextSplitter.builder().withMaxNumChunks(3000).withChunkSize(300).build();
        // ベクタストアに保存
        vectorStore.add(textSplitter.split(companyInfo));

        logger.info("CompanyInfo got loaded to VectorStore");
    }

}
```
Bean作成：
```java

 @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore){
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        // ベクタストアをロード
                        .vectorStore(vectorStore)
                        //ベクタストア検索で、類似道のしきい値を0.5に設定
                        .similarityThreshold(0.5)
                        // 一番合う三つの結果
                        .topK(3)
                        .build()).build();
    }

    @Bean("OpenAiChatClient")
    public ChatClient chatClient(OpenAiChatModel chatModel , RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
        ChatClient.Builder chatClient = ChatClient.builder(chatModel).defaultAdvisors(retrievalAugmentationAdvisor);
        return chatClient.build();
    }
    
    
```