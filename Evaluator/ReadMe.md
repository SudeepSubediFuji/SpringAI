# Evaluator
### 本プロジェクトではLLMを使って、現在作った関数・機能をテストしています。　

Evaluator法はAIの答え・レスポンスをチェックすることや結果の精度を確認することのために作れました。
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

### Evaluator種類
### RelevancyEvaluator
RelevancyEvaluatorはEvaluatorの一部です。RelevancyEvaluatorのデータ型はEvaluationResponseです。
RelevancyEvaluatorは

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

FactCheckingEvaluatorはRelevancyEvaluatorなように行動しますがそれだけはじゃないです。FactCheckingEvaluatorでは、各質問では、Fact（正しい情報）を設定して、そのFactとAIレスポンスを比べて数学的な結果を渡します。
</br>例えば：Factは書類やウェブアプリ内容（検索機能）を設定して、それは一番正しくとしてそのデータと比べます。
==普通ステップ==
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
==理想的な実装==
```java


```

