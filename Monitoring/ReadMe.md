## Monitoring
本MonitoringプロジェクトはRAGプロジェクトの上監視操作（Monitoring operation）を追加されています。
主に以下のことを監視するを目指します。
* メトリクス
* トレース
* AI的なインサイト

### Actuator
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

### Kibana監視シーツ



