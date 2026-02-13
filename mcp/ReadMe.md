## MCP

MCP-> Model・モデル + Context・コンテクスト + Protocol・プロトコル

MCPは、新しいプロトコルです。Httpsプロトコルの上に作って、あって、Httpの上に層を追加します（GraphQlに似ている）。
簡単で説明するとLLMには一つ課題があります。それは知識の課題です。LLMを使い以前では、知識をカットオフされています（メモリ、プロセスとリソースを普通の端末では足りない）。そんの課題を解決するため、MCPは入門をされました。

MCPは、主に２つ部品に分割されています。（構成的）
* MCPサーバ：MCPサーバは集中型システムです。ホストされたサーバでは、必要なツールやリソースを定義します。
* MCPクライアント：クライアントはMCPサーバと接続の依頼をして、要点な構成を設定されている場合、接続して、MCPサーバ側においてあるツールをクライアントの取得・共用して、もらえる。MCPクライアント側では他は機能・関数にツールを使って、適切な作業する。

※MCPサーバー側はSpringAIで設定するため、ToolCallBackProvider法 をLLM（チャットクライアント）に設定する必要があります。
MCPクライアント側はSpringAIでアプリに設定するため、ToolCallBackやToolCallBacks法を使って、ツールを設定する必要があります。（ツールをMCPクライアント側に公開する）

MCPアキーテクチャでは主な参加者は以下の通りです：
1. STDIO
ロカル的にMCPサーバーを呼び出すとSTDIO（Standard input/output）を入力と出力を読込する.(ロカル環境にセットアップする場合)。
・コマンドラインツール
・簡単なツールやスクリプト
・プロセス中でスムーズコミニケション

2. Streamable Http
インターネット親しく方。
Http ポストを使って、データを送ります。後はServer-Sent Events(SSE) を使って、サーバー側からクライアント側へに向いてストリームの反応をします。
・ウェブアプリやブラウザ的なツール
・同時なコミニケション
・複数ユーザーを同時に接続
・会話は複数リクエストでも行ける 。

3. SES：
Depricatedとサーバーからクライアントのストリングスの場合だけを使います。


#### MCPインスペクター：
MCPインスペクターはMCPサーバをテストとデバッグするため、使われています。
コマンド：
```
npx @modelcontextprotocol/inspector
```
上記のコマンドを実行したら、ブラウザに新しいlocalhostを開きます。そのブラウザでは、タランスポート型を設定して、必要フィリードに記入して、接続（Connect）ボータンを押せば接続します。
MCPインスペクターは三つ法で接続することできます（タランスポート層）。上記に書いているような同じです。
１．STDIO　２．Streamable Http　３．SES
※npxまたはDockerいずれはインストールするによって 重要です。

#### GitHub MCP サーバ
GitHub MCP サーバはGitHubのApiでファイル操作、レポジトリ管理、検索機能やなどなど操作するためのMCPサーバです。
SpringアプリでGitHubMCPサーバの構成（application.propertiesやconf.json、後は、個人の細粒度トークン）されたら接続可能です。
```
    "mcpServers":{
    "github": {
      "command": "docker",
      "args": [
        "run",
        "-i",
        "--rm",
        "-e",
        "GITHUB_PERSONAL_ACCESS_TOKEN",
        "mcp/github"
      ],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "自分の細粒度トークン"
      }
    }
    }
```
application.properties
```
#GitHubMCPサーバ構成ファイルのパースを書く
spring.ai.mcp.client.stdio.servers-configuration=classpath:mcp-servers.json
#mcpクライアント
spring.ai.mcp.client.enabled=true
```
参照プロジェクト：https://github.com/SudeepSubediFuji/SpringAI/tree/main/mcp/mcpserverstdio




参照：
* https://modelcontextprotocol.io/docs/getting-started/intro
* https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html