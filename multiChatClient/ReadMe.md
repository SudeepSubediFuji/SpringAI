## multiChatClient

本プロジェクトでは、
* チャットオプションの動作の確認
  * Thinkingモードを有効化するのは、チャットオプションを初期化して、.enableThinking()メソッドを設定します。
* 構造化出力を設定（ストリムレスポンスに変換、）
* チャットメモリの動作
* ユーザープロンプトテンプレートとシステムプロンプトテンプレートの作成方法
* service作成処理（トークン使用化）
* 複数チャットモデル操作
* H2データベースのセットアップをして、データはテーブルに保存 </br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;を行いました。

以下のセットアップはThinkingモードと設定と未有効化設定する手続きです。
```java
   private ChatClient ollamaChatClientTest;
   public ollamaChatOptionsController(@Qualifier("ollamaChatClientTest") ChatClient ollamaChatClientTest) {
       this.ollamaChatClientTest = ollamaChatClientTest;
   }
   
    @GetMapping("/thinkMode" )
    public String ollamaChatOptionsTest(@RequestParam("message") String message) {
        OllamaChatOptions chatOptions = OllamaChatOptions.builder().model("deepseek-r1:8b").temperature(0.7).enableThinking().build();
        return ollamaChatClientTest.prompt(message).options(chatOptions).advisors(new ollamaTokenUsageAuditAdvisor()).call().content();
    }
    @GetMapping("/fastMode" )
    public String ollamaChatOptionsFastTest(@RequestParam("message") String message) {
        OllamaChatOptions chatOptions = OllamaChatOptions.builder().model("gemma3:1b").temperature(0.7).disableThinking().build();
        return ollamaChatClientTest.prompt(message).options(chatOptions).advisors(new ollamaTokenUsageAuditAdvisor()).call().content();
    }
```
※Thinkingモードを有効化するには、適切なモデルを選ぶ必要があります。上記では、deepseek-r1:8b AIモデルを設定するの理由はdeepseek-r1のThinkingモードは、軽くて、Thinkingモードのため、知識を練習されて、最近は最も人気です。（Thinkingモードは軽くて無料AIモデルはあまりない）
※ gemma3:1bのFastモードも軽くて、ThinkingモードではなくFast・早めに答え・レスポンスをするのです。

参照資料：
このプロジェクトでは、実施した操作について、説明は 既に以下のリンクに書いてあります。
https://github.com/SudeepSubediFuji/SpringAI/blob/main/README.md

https://github.com/SudeepSubediFuji/SpringAI/blob/main/openAI/ReadMe.md
