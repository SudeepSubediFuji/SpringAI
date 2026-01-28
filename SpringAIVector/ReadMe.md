# 実行ビルド構成の設定するため、Intellij　Ideaで、以下の設定を行ってください。 

１．Intellij IdeaでCloneされたフォルダを開く

２．Intellij　Ideaの左上のメニューにある「現在のファイル🔽」というボータンをクリックをして、
構成の編集ボタンを表示されたら、
そこをクリックをしたら実行/デバッグ構成がポップアップ が出ます。

３．左上の「＋」ボタンをクリックをして、「新規構成を追加」を表示されます。
そこからアプリケーションを選んでください。

４．以下のように設定
名前:　SpringAiVectorApplication
ビルドと実行：
Java盤：21
メインクラス：com.example.springaivector.SpringAiVectorApplication
環境変数：OPENAI_API_KEY=あなたのOpenAIのAPIキーを入力してください
使用するモジュール：SpringAiVector

５．適用をクリックをして、OKをクリックをしてください。

６．Intellij Ideaの右上の実行ボタンをクリックをして、アプリケーションを実行してください。
例：
![img.png](img.png)

## 注意点：
1. OpenAIのAPIキーを取得して、環境変数に設定する必要があります。
2. Mavenがインストールされていることを確認してください。
3. Java 21がインストールされていることを確認してください。
4. プロジェクトの依存関係が正しく解決されていることを確認してください。

本ポロジェクトを実行する方法：
Git clone をして、そこのフォルダパスにタミヤで入って、以下のコマンドを実行してください。

```
# テストをスキップしてビルド
mvn clean install -DskipTests
```

その後、Intellij Ideaで上記の設定を行い、アプリケーションを実行してください。
以下のリンクから、Qdrantのダッシュボードにアクセスして、コレクションが作成されていることを確認できます。
http://localhost:6333/dashboard#/collections


