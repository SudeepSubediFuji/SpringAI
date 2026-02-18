# Evaluator
### 本プロジェクトではLLMを使って、現在作った関数・機能をテストしています。　

EvaluatorインターフェースはAIの答え・レスポンスをチェックすることや結果の精度を確認することのために作れました。
AIでは、いくつがエラーを出る可能性がありますのでEvaluator法を使って、安全層を追加します。

流れ：
1. Chat API ＝＞　質問（プロンプト）→　LLM
    LLM => 答え・レスポンス
2. Evaluator系　＝＞　質問（ユーザープロンプト）＋　答え・レスポンス　→　LLM
    LLM =>　計算結果

アナロジー：
Evaluatorでは、ユーザープロンプトとLLM側の答え・レスポンスは正しいかどうかを確認するに当たって、もう一度LLM側にユーザープロンプトと以前のLLM側からのレスポンスを確認します。

Use Case:
ロカル環境にあるLLMやLLMの知識をテストするようにしたい場合、Evaluatorは最も重要です。

### Evaluatorの実装されたクラス
### RelevancyEvaluator
* RelevancyEvaluatorのデータ型はEvaluationResponseです。
* RelevancyEvaluatorはチャットボット、検索機能AI、などテストするため、理想です。
* 関連性チェックする。

以下はRelevancyEvaluatorを操作するための手順です。
```java
// 質問
String Question = "日本の大文字は何？";
//LLMからのレスポンス
String AiResponse = ChatClient.build().user(Question).content().call();
RelivancyEvaluator relivancyEvaluator;
// EvaluationRequest(Question,List.of(),AiResponse) -> EvaluationRequestは二つや三つパラメータを受けます。
//　二つ：　ユーザープロンプト　、　AIレスポンス
//　三つ：　ユーザープロンプト　、比べたいデータ（List型）、　AIレスポンス

EvaluationRequest evaluationRequest = new EvaluationRequest(Question,List.of(),AiResponse);
EvaluationResponse evaluationResponse = relivancyEvaluator.evaluate(evaluationRequest);
if(evaluationResponse.isPass()){
    System.out.println("テストを合格しました。");
}else{
    System.out.println("テストを失敗しました。");
}
```
以下はも一度比べるに当たって、与えるプロンプトです。
```
裏側：
DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("""
				Your task is to evaluate if the response for the query
				is in line with the context information provided.

				You have two options to answer. Either YES or NO.

				Answer YES, if the response for the query
				is in line with context information otherwise NO.

				Query:
				{query}

				Response:
				{response}

				Context:
				{context}

				Answer:
			""");

```

### FactCheckingEvaluator

FactCheckingEvaluatorはRelevancyEvaluatorなように行動しますがそれだけはじゃないです。
* FactCheckingEvaluatorでは、各質問では、Fact（正しい情報）を設定して、そのFactとAIレスポンスを比べて数学的な結果を渡します。
* 病院、金融機関、法律機関に対応するAIでは、FactCheckingEvaluatorは理想的なチェックする。
* コンテクスト（書類）を集中します(RAG関連)。
</br>例えば：Factは書類やウェブアプリ内容（検索機能）。。などなどを設定して、それは一番正しくとしてそのデータと比べます。
#### 普通ステップ1.0.0盤
```java
// 質問
String Question = "日本の大文字は何？";
String AiResponse = ChatClient.build().user(Question).content().call();

FactCheckingEvaluator factCheckingEvaluator;

EvaluationRequest evaluationRequest = new EvaluationRequest(Question,AiResponse);
EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
if(evaluationResponse.isPass()){
    System.out.println("テストを合格しました。");
}else{
    System.out.println("テストを失敗しました。");
}

```

#### 理想的な実装2.0.0盤
2.0.0盤では、FactCheckingEvaluatorはProtected型で設定されています。後は、正確的にFactCheckingEvaluatorを実装するため、カスタムCheckerを作って、FactCheckingEvaluatorをExtendして、やり取りに変わったらしいです。
```java

public class CustomFactCheckingEvalutor extends FactCheckingEvaluator {

    public CustomFactCheckingEvalutor(@Qualifier("openAiBuilder") ChatClient.Builder chatClientBuilder) {
        super(chatClientBuilder,"""
			Evaluate whether or not the following claim is supported by the provided document.
				Respond with "yes" if the claim is supported, or "no" if it is not.

				Document:
				{document}

				Claim:
				{claim}
			""");
    }
}
// 上記のDocumentとは与える書類や正しく情報（決まった）、Claim→AIレスポンス
```
Loader・ロードル使い場合、以下の実装します。
書類（正しく情報として）をインポートして、List<Document>やListに変換（TikaDocument依存を追加する必要）
```java
@Value("classpath:/Documents/Japan.pdf")
Resource GroundTruth;
TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(GroundTruth);
List<Document>Doc = tikaDocumentReader.get();
EvaluationRequest evaluationRequest = new EvaluationRequest(question, Doc, answer);
EvaluationResponse evaluationResponse = customFactCheckingEvalutor.evaluate(evaluationRequest);
if(evaluationResponse.isPass()){
    System.out.println("テストを合格しました。");
}else{
    System.out.println("テストを失敗しました。");
}
```
簡単な種類をロードする方法
```java
@Value("classpath:/Documents/HrPolicy.pdf")
Resource HrPolicy;
 String hrpolicy = HrPolicy.getContentAsString(StandardCharsets.UTF_8);
 EvaluationRequest evaluationRequest = new EvaluationRequest(question, List.of(new Document(hrpolicy)), aiResponse);
 logger.info("Evaluation getResponseContext :" + evaluationRequest.getResponseContent() + "Evaluation getUserText :" + evaluationRequest.getUserText());
 EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
```

