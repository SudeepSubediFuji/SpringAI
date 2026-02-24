## openAI

本プロジェクトは複数チャットモデルを使って、実行する方法は記入しました。

以下の通りBeanを設定すれば、Qualifierにも特定なビルダーを設定すると特的なApiに公開可能です。
```java

   @Bean("openAiChatClientBuilder")
    public ChatClient.Builder openAiChatClientBuilder(OpenAiChatModel chatModel){
        return ChatClient.builder(chatModel);
    }
    @Bean("ollamaChatClientBuilder")
    public ChatClient.Builder ollamaChatClientBuilder(OllamaChatModel chatModel){
        return ChatClient.builder(chatModel);
    }
```
コントローラ：
```java
private final ChatClient openAIChatClient;
private final ChatClient ollamaAIChatClient;

public ChatClientController(@Qualifier("openAiChatClientBuilder") ChatClient.Builder openAiChatClientBuilder,
                            @Qualifier("ollamaChatClientBuilder") ChatClient.Builder ollamaChatClientBuilder) {
    this.openAIChatClient = openAiChatClientBuilder.build();
    this.ollamaAIChatClient = ollamaChatClientBuilder.build();
}
@GetMapping("/openai-chat")
public String chat(@RequestParam("message") String message) {
    return openAIChatClient.prompt(message).call().content();
}
@GetMapping("/ollama-chat")
public String chat(@RequestParam("message") String message) {
    return ollamaAIChatClient.prompt(message).call().content();
}
```

本プロジェクトでは、プロパティファイルは三つがあります。
* application.properties　→　DockerでOllamaのみを立ち上げる。
* application.properties_old　→　同様にOllama（Dockerで公開する）とOpenAI（キーを使う）。※構成設定.mdファイルに書いた通りで設定してください。

