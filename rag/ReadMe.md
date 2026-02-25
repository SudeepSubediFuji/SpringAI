## ragプロジェクト

本プロジェクトでは、ヘルプデスクAIでチケット・切符を作成するプロジェクトです。
本プロジェクトで、行った主な処理は以下の通りです：
* サービス、ツール
* RAG操作： 書類ロードル、ウェブ検索 、日付を取得処理

### pdfファイルをベクタデータベースに読み込みする処理

1. 一般的なロード処理：
```java
    private final VectorStore vectorStore;
    Logger logger = Logger.getLogger(FullPdfLoader.class.getName());
    public FullPdfLoader(VectorStore vectorStore){
        this.vectorStore=vectorStore;
    }

    @Value("classpath:/Documents/CompanyInfo.pdf")
    Resource CompanyInfo;

//    @PostConstruct
    public void pdfLoader(){
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(CompanyInfo);
        List<Document> companyInfo = tikaDocumentReader.get();
        vectorStore.add(companyInfo);
        logger.info("CompanyInfo got loaded to VectorStore");
    }
```
上記では、List<Document> companyInfo = tikaDocumentReader.get()をして、そのままでベクタデータベースにデータを保存しました（vectorStore.add(companyInfo)）。こういう感じで保存するのは、だめじゃないですがトークン使用化は高いです。
理由は全部データは一般的に読み込んで一つコレクションに保存ちゃってしまいます。そうなると毎リクエストでは、全部コレクションの内容を確認するようにしますので、トークン使用化が高いです。
2. チャンクに分かれて、ベクタデータベースに保存：
```java
   // pdfファイルパス
    @Value("classpath:documents/Eazybytes_HR_Policies.pdf")
    Resource hrPolicy;

    private final VectorStore vectorStore;

    public pdfLoaderRag(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void policyLoader() {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(hrPolicy);
        // Documentオブジェクト
        List<Document> docs = tikaDocumentReader.get();
        // withChunkSize() →　チャンクサイズ　→　トークンサイズ。200トークン
        // withMaxNumChunks() → 最大チャンク数　→　最大トークンサイズ　→　400
        TextSplitter textSplitter = TokenTextSplitter.builder().withChunkSize(200).withMaxNumChunks(400).build();
        vectorStore.add(textSplitter.split(docs));
    }
```
上記にの①番の問題 → 高いトークン使用化の解決はその書類は分割して、いくのです。そのような処理はチャンキングと言います。以下の2番目では、ちゃんとにチャンクサイズを設定されています。TokenTextSplitterメソッドを使って、チャンクに分かれます。
![img_1.png](img_1.png)
上の画像を見るとEazybytes_HR_Policies.pdfファイルはPointsに分かれております。
例えば：
Eazybytes_HR_Policies.pdf　→　「Pointアドレス0→チャンク＿インデックス：０、Pointアドレス１→チャンク＿インデックス：１、Pointアドレス２→チャンク＿インデックス：２、Pointアドレス３→チャンク＿インデックス：３、Pointアドレス４→チャンク＿インデックス：４、Pointアドレス５→チャンク＿インデックス：５」
全部＿チャンク　⇒　6.

### ウェブ検索RAG

以下はWebSearchDocumentRetrieverクラスです。TavilyAIエージェントと接続するための処理です。
```java

public class WebSearchDocumentRetriever implements DocumentRetriever {

    Logger logger = Logger.getLogger(WebSearchDocumentRetriever.class.getName());

    private static final String TAVILY_API_KEY = "TAVILY_API_KEY";
    private static final String TAVILY_BASE_URL = "https://api.tavily.com/search";
    private static final int DEFAULT_RESULT_LIMIT = 5;
    private final int resultLimit;
    private final RestClient restClient;

    public WebSearchDocumentRetriever(RestClient.Builder restClientBuilder, int resultLimit) {
        // Tavily AIエージェントのHttpロギング認証の準備
        Assert.notNull(restClientBuilder, "Client builder cannot be null");
        String API_KEY = System.getenv(TAVILY_API_KEY);
        Assert.hasText(API_KEY, "Environment variable " + TAVILY_API_KEY + " must be set.");
        this.restClient = restClientBuilder
                .baseUrl(TAVILY_BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
                .build();
        if (resultLimit <= 0) {
            throw new IllegalArgumentException("resultLimit must be greater than o");
        }
        this.resultLimit = resultLimit;
    }

    // Tavily実行処理
    @Override
    public List<Document> retrieve(Query query) {
        logger.info("Processing Query: {}" + query.text());
        Assert.notNull(query, "Query cannot be null.");

        String q = query.text();
        Assert.hasText(q, "Query.text() cannot be null.");
        //.body(TavilyResponsePayload.class) --> here this method will typeCast the responsePayload
        TavilyResponsePayload responsePayload = restClient.post()
                .body(new TavilyRequestPayload(q, "advanced", resultLimit))
                .retrieve()
                .body(TavilyResponsePayload.class);
        // payloadが空とnullの確認
        if (responsePayload == null || CollectionUtils.isEmpty(responsePayload.results())) {
            return List.of();
        }
        // payloadが空とnullじゃない場合、Documentのサイズとして、保存
        List<Document> documents = new ArrayList<>(responsePayload.results.size());
        for (TavilyResponsePayload.Hit hit : responsePayload.results()) {
            // 各Tavilyヒット（ボータンを押すった時）をSpringAiDocumentとして、メタデータとスコア含めて保存
            Document doc = Document.builder()
                    .text(hit.content())
                    .score(hit.score())
                    .metadata("title", hit.title)
                    .metadata("url", hit.url)
                    .build();
            documents.add(doc);
        }
        return documents;
    }

    // リクエストPayload定義
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record TavilyRequestPayload(String query, String searchDepth, int maxResult) {
    }
    // レスポンスPayload定義
    record TavilyResponsePayload(List<Hit> results) {
        record Hit(String title, String url, String content, Double score) {
        }
    }

    //builderメソッド定義
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private RestClient.Builder restClientBuilder;
        private int resultLimit = DEFAULT_RESULT_LIMIT;

        private Builder() {
        }


        public Builder restClientBuilder(RestClient.Builder restClientBuilder) {
            this.restClientBuilder = restClientBuilder;
            return this;
        }

        public Builder maxResults(int maxResults) {
            if (maxResults <= 0) {
                throw new IllegalArgumentException("maxResults is not set. ");
            }
            this.resultLimit = maxResults;
            return this;
        }

        public WebSearchDocumentRetriever build() {
            return new WebSearchDocumentRetriever(restClientBuilder, resultLimit);
        }
    }

}
```
※上記クラスいきなり実装するのは不可雑ですが本当はVectorStoreDocumentRetrieverのクラスを下げして、そのような実装行っていいです。（参照クラス：VectorStoreDocumentRetriever implements DocumentRetriever）
以下では、上のWebSearchDocumentRetrieverクラスを読んで.documentRetriever()メソッドのパラメータとして、アドバイザーを作って、ビルドします。しかし、実装は他の方法でも可能です。例えば関数やツールを作成して、実装。
```java
public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                                 RestClient.Builder restClientBuilder,
                                 ChatMemory chatMemory) {
        QueryTransformer queryTransformer = TranslationQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder.clone())
                // レスポンス・答えの言語　→　日本語
                .targetLanguage("japanese")
                .build();

        var webSearchRagAdvisor =
                RetrievalAugmentationAdvisor.builder()
                        .documentRetriever(WebSearchDocumentRetriever.builder().restClientBuilder(restClientBuilder).maxResults(5).build())
                        .queryTransformers(queryTransformer)
                        .build();
        Advisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return chatClientBuilder
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), webSearchRagAdvisor, new TokenUsageAuditAdvisor(), chatMemoryAdvisor))
                .build();
    }
```

### 現在の時間・日付取る方法
LLMは知識カットオフされていますので、現在時間や日付はわからないです。LLMは現在時間や日付を分かるようにするため、ツールを作って、Systemやロカル時間を取られます。

1. 日付ツール定義
```java
//日付ツール
public class DateTimeTools {
    Logger logger = Logger.getLogger(DateTimeTools.class.getName());
    // 
    @Tool(name = "getCurrentDateTime", description = "Current date and time provider tool")
    String getCurrentDateTime(@ToolParam(
            description = "Timezone") String timeZone) {
        logger.info("LocaleDateTime: " + LocalDateTime.now(ZoneId.of(timeZone)).toString());
        return LocalDateTime.now(ZoneId.of(timeZone)).toString();
    }
}
```
※timezone設定しなくてもいいですが、しないとは、日本・ロカルTimezoneとして、日付を取ります。Timezoneも設定すると他の所の時間も正しく上げます。
2. ツール設定 
</br>2\.1. Bean作成（デフォルトツールとして設定）
```java
@Configuration
public class TimeChatClientConfig {
    // 
    @Bean("TimeChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,ChatMemory chatMemory){
        ChatOptions chatOptions = ChatOptions.builder().model("gpt-5-mini").build();
        Advisor chatmemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultOptions(chatOptions)
                .defaultTools(new DateTimeTools())
                .defaultAdvisors(new SimpleLoggerAdvisor(),new TokenUsageAuditAdvisor(),chatmemoryAdvisor)
                .build();
    }

}
```
   ChatClientBuilderでは、defaultTools()メソッドがあってそこにパラメータとして、DateTimeTools()のオブジェクトを設定する。
   </br> 2\.2. コントローラ的なツール設定（コントローラにチャットクライアントのツールメソッドにツール設定）

```java
@RestController
@RequestMapping
public class TimeController {
    private final ChatClient chatClient;

    public TimeController(@Qualifier("TimeChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/time")
    public ResponseEntity<String> getTime(@RequestHeader("username") String username,
                                          @RequestParam("message") String message) {
        String answer = chatClient.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                // DateTimeToolsのオブジェクト
                .tools(new DateTimeTools())
                .call().content();
        return ResponseEntity.ok(answer);
    }
}
```

### HelpDestTicket
ヘルプデスクチケット作成流れ：
1. entity定義　：　テーブル名、行名や列名設定 →　チケットデータ型設定。: helpDeskTicket
2. model定義　：　テーブル　representation→　DBに保存するため、モデルを設定する : 
TicketRequest　：　依頼されたチケット作成するためのレコードレポジトリ
2. repository定義　：　(HelpDeskRepositoryはJdbcRepositoryを実装して、) : 
HelpDeskRepository -> HelpDeskRepository.findByUsername -> 自動的にユーザー名で検索
3. service定義: TicketRequestレコードを使って、サービスを作る。: 
HelpDeskService　→　HelpDeskService.createTicket　→ builderを使って、チケット作成を行う
4. tools定義：　サービスを使って、ツール作成する。: HelpDeskTicketTools
5. ツール設定（デフォルトツールやChatClientを呼ぶ時ツール設定）: HelpDeskConfig, HelpDeskManageController.
HelpDeskTicketToolツール関数：   createTicket,getTicketByStatus

チャットクライアントで構成設定する時、デフォルトツールやコントローラ側でツール読んでHelpDeskTicketToolのオブジェクトを設定しています。
1. createTicket　→　HelpDeskService.createTicket
2. getTicketByStatus　→　HelpDeskRepository.findByUsername

Restコントローラをちゃんとに作成していないですがAIを理解して、チケット作成します。




