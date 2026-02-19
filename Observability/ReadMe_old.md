## Monitoring
本MonitoringプロジェクトはRAGプロジェクトの上監視操作（Monitoring operation）を追加されています。
主に以下のことを監視するを目指します。
* メトリクス
* トレース
* AI的なインサイト
上記はテレメトリーデータで呼びます。

## Actuator : 可観測性
Actuator依存を使って、メトリクスを収集することできます。Springでは、可観測性機能を使いに当たって、Actuator依存を追加する重要です。
#### 1.内部システムの色々ことを監視するに当たって、Springでは、actuator依存追加が必要です。
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```
#### 2.公開エンドポイントを構成する：
```properties
management.endpoints.web.exposure.include=health,metrics
```
#### 3.メトリクスをアクセスエンドポイント
``` 
//全部メトリクス
http://localhost:8080/actuator/metrics
```

### Prometheus監視シーツ

#### 1.prometheus依存
```xml
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>
```
※忘れらず依存を追加後は、Meavenの変更を同期ボタン押してください。

#### 2.prometheusのエンドポイントを公開
```properties
management.endpoints.web.exposure.include=health,metrics,prometheus
```
#### 3.Dockerでprometheusをホストされたい（Dockerコンテナイメージ）場合は、
```yaml
service:
  prometheus:
    image: prom/prometheus
    container_name: prometheus
    volumes:
      - "./prometheus-config.yml:/etc/prometheus/prometheus.yml"
    networks:
      - spring-ai
    ports:
      - 9090:9090
networks:
  spring-ai:
    -bridge
```
※上記にVolumesで./prometheus-config.ymlが設定しましたので、コンテナイメージを作るための構成ファイルです。３盤に内容を含めています。
#### 4.prometheusDockerコンテナイメージの構成ファイル内容
```yaml
global:
  scrape_interval: 5s
  evaluation_interval: 5s
  
scrape_configs:
  - job_name: spring-ai
    metrics_path: /actuator/prometheus
    scrape_interval: 5s
    static_configs:
      - targets: ['host.docker.internal:8080']
```
参照：https://prometheus.io/docs/prometheus/latest/configuration/configuration/
#### 5.正確に動いたら以下のリンクに開いたらprometheusのダッシュボードを表示されま。
```
// prometheusを公開した場合
http://localhost:8080/actuator/prometheus
```
#### 他：
Prometheusの依存追加して、動いたら自動的に色々なメトリクスを作ってもらいますがSpringアプリで使ったエンドポイントを最初は公開して、ください。
そうしないとprometheusが見つからないです（監視）。
Prometheusは情報収集するめに、最初はそのサービス、ツールやエンドポイントは1回でも動く必須です。

### Grafana監視シーツ
Prometheusでは、GUIがありませんので、grafanaを構成するとGUIようなインターフェースで可観測性することが可能です。Prometheusを追加したら、その同じPrometheusをデータソースとして、grafanaを使われます（他データソース、監視ツール、などでも可能です。例えば：Sqlデータベース、Influxデータベース,elasticsearch(ローグ監視)）。
KibanaはDockerで公開するため、以下のようにYamlファイルを追加してください。
```yaml
services:
  grafana:
    image: grafana/grafana
    container_name: grafana
    volumes:
      - grafana-storage:/var/lib/grafana
    ports:
      - 3000:3000
    networks:
      - spring-ai
networks:
  spring-ai: 
    driver: bridge
    
volumes:
  grafana-storage:
```
※volumesを設定したらGrafanaを再起動しても保存したデータがなくなりません。
後は、prometheus（データソースとして）とGrafanaを接続して、作りたい場合、networksの情報は合わせないとだめです。

## トレーシング

Metrics tell you what happened. Tracing tells you where,when,and how it happened. 
Just like flight data recorders track an aircraft's journey, tracing tools track requests through your app -end to end



