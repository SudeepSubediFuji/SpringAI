## TextToSpeech

## 音声生成・音楽生成
音声からテクストに変換する。日本語音声から英語テクストへの変換も可能です（逆もまだ同様）。
SpringAIで、音声に対して、主に機能は以下の通りです：
1. テクストから音声の変換：
   SpeechModelやOpenAiAudioSpeechModelを使って、音声からテクスト側に変換することをできます。出力したい音声・運額のフォーマット、声、声のスピードとモデルを選ぶことを可能です。
しかしOpenAI側で返却するデータ型はTextToSpeechResponseですので、TextToSpeechResponseでByte「」型に変わって、ファイルライターでmp3や他の臨んだフォーマットに変換して良いです（以下に実装例をみてください）。
例：
```java
OpenAiAudioSpeechModel openAiAudioSpeechModel;
@GetMapping("/custom-speech")
String speech2(@RequestParam("message") String message) throws IOException {

    OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                                                                //モデル
                                                                .model("gpt-4o-mini-tts")
                                                                //音声の声
                                                                .voice("coral")
                                                                //声のスピード
                                                                .speed(1.25)
                                                                //レスポンスフォーマット
                                                                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3).build();
    TextToSpeechResponse textToSpeechResponse = openAiAudioSpeechModel.call(new TextToSpeechPrompt(message, options));

    byte[] audioByte = textToSpeechResponse.getResult().getOutput();
    Path path = Paths.get("output.mp3");
    Files.write(path, audioByte, StandardOpenOption.TRUNCATE_EXISTING);
    return "Audio file is created Successfully!. " + path.toAbsolutePath();
}
```

2. 編訳者ような言語を変換・音声からテクストへの変換
OpenAiAudioTranscriptionModelを使って、オーディオからテクストへの変換を可能です。テクストは色々な言語に変換することもできます。プロンプトを半分一つの言語、残りは他の言語（OpenAIからサポートされた）を与えても変換は可能です。
ですが他の言語に変わるため、OpenAiAudioTranscriptionOptionsといTranscriptionOptionを追加する必要があります。OpenAiAudioTranscriptionOptionsオプションを使って、オーディオは何について情報を含めているのかはヒントを上げることをできます。
例：
```java
OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

 @GetMapping("/audio")
    String transcibe3(@Value("classpath:Audio/JPOpenAI.mp3") Resource Audio) {
        var audioTranscriptResponse = openAiAudioTranscriptionModel.call(
                new AudioTranscriptionPrompt(Audio,
                        OpenAiAudioTranscriptionOptions.builder()
                                // prompt("オーディオについて、ヒント").language("言語")
                                .prompt("Talking about OpenAI").language("ja")
                                .temperature(0.5f)
                                // 望んでいるレスポンスフォーマット
                                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.VTT)
                                .build()));
        return audioTranscriptResponse.getResult().getOutput();
    }
```
※上記では、openAiAudioSpeechModelやopenAiAudioTranscriptionModelを使っていますがSpeechModel（現在：TextToSpeechModel）や
参照書類：https://developers.openai.com/api/reference/resources/audio
## 画像生成
SpringAIでは、画像生成を行うためはImageModelを使います。
※OpenAiImageModelはImageModelを継承されています。
簡単なImageModelの実装例：
```java
ImageModel imageModel;
    @GetMapping("/image")
    public ImageResponse imageGen(@RequestParam("message") String message) {
        return imageModel.call(new ImagePrompt(message));
    }
```
プロンプトを設定して、イメージモデルを呼び出して、結果はB64Jsonに取る方法合っても、取ったら空しが返却しません（まだ出来ない）。現在はイメージモデルだけを呼び出したら.getUrl()メソッドでイメージ生成が可能ですがURLに開いて、確認できます。
```java
imageModel.call(new ImagePrompt(message)).getResult().getOutput().getB64Json();
```
そういうことでImageModelは呼び出して、イメージオプションを初期化して、.getResponseFormat("b64_json")メソッドを設定して、イメージモデルをこーるするところで.getResult().getOutput().getB64Json()を設定するとb64Jsonでリターンできます。
b64Jsonフォーマットよりbyte「」フォーマットに変換して（byte[] image = Base64.getDecoder().decode(b64Json)）、ファイルライターを使って、希望のパスに保存可能です。
ImageOptionを含めて実装例：
```java
ImageModel imageModel;
@GetMapping(value = "/custom-image2", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateImageWithOptions(@RequestParam("message") String message) throws IOException {

        String b64Json = imageModel.call(new ImagePrompt(message,
                OpenAiImageOptions.builder()
                        .N(1)
                        .quality("hd")
                        .style("natural")
                        .height(1024)
                        .width(1024)
                        .responseFormat("b64_json")
                        .build())).getResult().getOutput().getB64Json();

        Path path = Paths.get("image.png");
        byte[] image = Base64.getDecoder().decode(b64Json);
        Files.write(path, image);
        path.toAbsolutePath();
        return Base64.getDecoder().decode(b64Json);
    }
```



