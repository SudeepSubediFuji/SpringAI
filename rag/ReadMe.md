## RAG
本プロジェクトはRAGの操作を行います。

## 実行ビルド構成の設定するため、Intellij　Ideaで、以下の設定を行ってください。
1. Intellij IdeaでCloneされたフォルダを開く
2. Intellij　Ideaの左上のメニューにある「現在のファイル🔽」というボータンをクリックをして、
   構成の編集ボタンを表示されたら、 そこをクリックをしたら実行/デバッグ構成がポップアップ が出ます。
3. 左上の「＋」ボタンをクリックをして、「新規構成を追加」を表示されます。
   そこからアプリケーションを選んでください。
4. 以下のように設定
   名前:　MultiChatClientApplication</br>
   ビルドと実行：</br>
   Java盤：21</br>
   メインクラス：com.example.rag.RagApplication</br>
   環境変数：OPENAI_API_KEY=自分のOpenAIのAPIキー</br>
　　　　　　　TAVILY_API_KEY＝自分のTavilyAIエージェントのAPIキー</br>
　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　を入力してください</br>
※入力文式：``` OPENAI_API_KEY=Apiキー；TAVILY_API_KEY=Apiキー; ```</br>
   使用するモジュール：SpringAiVector

5. 適用をクリックをして、OKをクリックをしてください。
6. Intellij Ideaの右上の実行ボタンをクリックをして、アプリケーションを実行してください。
   例：
   ![img.png](img.png)

## 注意点：
1. OpenAIのAPIキーとTavilyのApiキーを取得して、環境変数に設定する必要があります。
以下のリンクに開いて、
  TavilyAI エージェント： https://auth.tavily.com/u/signup/identifier?state=hKFo2SB5TE1IdFpjQlZiQU15d1lzdFBWeTNZRFA4YVpjenZYWqFur3VuaXZlcnNhbC1sb2dpbqN0aWTZIHJxeS0xWFN4WkNRY2ZvQ0hyTm1tZzJHbGcyNHhla3pto2NpZNkgUlJJQXZ2WE5GeHBmVFdJb3pYMW1YcUxueVVtWVNUclE
  OpenApi: https://platform.openai.com/docs/overview
  ※TavilyAIエージェントはアカウントを作成して、無料盤を使って良い。（注意：一か月で1000クレジット無料）
　※OpenAIは無料Apiキーで足りない可能性があり、$5のキーを買って良い。
2. Mavenがインストールされていることを確認してください。
3. Java 21がインストールされていることを確認してください。
4. プロジェクトの依存関係が正しく解決されていることを確認してください。
5. OllamaのAPIを使用したい場合、Ollamaのインストールとモデルのダウンロードが必要です。(おすすめ：https://ollama.com/library/llama3.2:1b)
   ※OllamaのAPIキーは不要です。Ollamaはローカルで動作します。移動しないとOllamaのAPIを使用できません。

本ポロジェクトを実行する方法：
Git clone をして、そこのフォルダパスにタミヤで入って、以下のコマンドを実行してください。

```
# テストをスキップしてビルド
mvn clean install -DskipTests
```

その後、Intellij Ideaで上記の設定を行い、アプリケーションを実行してください。
以下のリンクから、Qdrantのダッシュボードにアクセスして、コレクションが作成されていることを確認できます。
http://localhost:6333/dashboard#/collections




