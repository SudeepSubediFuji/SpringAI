## MCP プロジェクト

## 実行ビルド構成の設定するため、Intellij　Ideaで、以下の設定を行ってください。
1. Intellij IdeaでCloneされたフォルダを開く
2. Intellij　Ideaの左上のメニューにある「現在のファイル🔽」というボータンをクリックをして、
   構成の編集ボタンを表示されたら、 そこをクリックをしたら実行/デバッグ構成がポップアップ が出ます。
3. 左上の「＋」ボタンをクリックをして、「新規構成を追加」を表示されます。
   そこからアプリケーションを選んでください。
4. 以下のように設定
   名前:　McpserverstdioApplication
   ビルドと実行：
   Java盤：21
   メインクラス：com.fuji.mcpserverRemote.McpserverstdioApplication
   環境変数：OPENAI_API_KEY=あなたのOpenAIのAPIキーを入力してください
   使用するモジュール：McpserverstdioApplication
5. 適用をクリックをして、OKをクリックをしてください。
6. Intellij Ideaの右上の実行ボタンをクリックをして、アプリケーションを実行してください。
   例：
   ![img.png](img.png)

## 注意点：
1. OpenAIのAPIキーを取得して、環境変数に設定する必要があります。
2. Mavenがインストールされていることを確認してください。
3. Java 21がインストールされていることを確認してください。
4. プロジェクトの依存関係が正しく解決されていることを確認してください。
5. 既にnpxとnpmをインストールしないと動かないので右側のリンクからダウンロードして、インストールしてください。(ダウンロードリンク：https://nodejs.org/en/download)
6. GitHubで自分のアカウントで開いて、個人アクセストークンを生成してください。（設定→開発者設定→個人アクセストークン→細粒度コクーン→新しいトークンを生成する）
   トークンを生成した後は、src/main/resources/mcp-servers.jsonの環境変数のGITHUB_PERSONAL_ACCESS_TOKENの値に設定してください。
7. mcp-servers.jsonファイルにGitHub　MCPサーバの設定を記載されていますがDockerを動く必要があります。（裏でDockerデスクトップを動いてください）


本ポロジェクトを実行する方法：
Git clone をして、そこのフォルダパスにタミヤで入って、以下のコマンドを実行してください。

```
# テストをスキップしてビルド
mvn clean install -DskipTests
```

その後、Intellij Ideaで上記の設定を行い、アプリケーションを実行してください。

