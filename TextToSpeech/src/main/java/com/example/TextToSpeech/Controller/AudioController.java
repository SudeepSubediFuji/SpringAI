package com.example.TextToSpeech.Controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RestController
@RequestMapping
public class AudioController {
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;
    private final TextToSpeechModel textToSpeechModel;

    AudioController(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel, OpenAiAudioSpeechModel openAiAudioSpeechModel, TextToSpeechModel textToSpeechModel) {
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
        this.textToSpeechModel = textToSpeechModel;
    }

    @GetMapping("/audio")
    String transcibe(@Value("classpath:Audio/OpenAI.mp3") Resource Audio) {
        return openAiAudioTranscriptionModel.call(Audio);
    }

    // AudioTranscriptionPrompt
    @GetMapping("/audio-options")
    String transcibe2(@Value("classpath:Audio/OpenAI.mp3") Resource Audio) {
        var audioTranscriptResponse = openAiAudioTranscriptionModel.call(
                new AudioTranscriptionPrompt(Audio,
                        OpenAiAudioTranscriptionOptions.builder()
                                .prompt("Talking about OpenAI").language("en")
                                .temperature(0.5f)
                                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.VTT)
                                .build()));
        return audioTranscriptResponse.getResult().getOutput();
    }

    // AudioTranscriptionPrompt
    @GetMapping("/audio-jp")
    String transcibe3(@Value("classpath:Audio/JPOpenAI.mp3") Resource Audio) {
        var audioTranscriptResponse = openAiAudioTranscriptionModel.call(
                new AudioTranscriptionPrompt(Audio,
                        OpenAiAudioTranscriptionOptions.builder()
                                .prompt("Talking about OpenAI").language("ja")
                                .temperature(0.5f)
                                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.VTT)
                                .build()));
        return audioTranscriptResponse.getResult().getOutput();
    }

    @GetMapping("/speech")
    String speech(@RequestParam("message") String message) throws IOException {
        TextToSpeechPrompt prompt = new TextToSpeechPrompt(message);
        TextToSpeechResponse textToSpeechResponse = openAiAudioSpeechModel.call(prompt);
        byte[] audioByte = textToSpeechResponse.getResult().getOutput();
        Path path = Paths.get("output.mp3");
        Files.write(path, audioByte, StandardOpenOption.TRUNCATE_EXISTING);
        return "Audio file is created Successfully!. " + path.toAbsolutePath();
    }


    @GetMapping("/custom-speech")
    String speech2(@RequestParam("message") String message) throws IOException {

        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder().model("gpt-4o-mini-tts").voice("coral").speed(1.25).responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3).build();
        TextToSpeechResponse textToSpeechResponse = openAiAudioSpeechModel.call(new TextToSpeechPrompt(message, options));

        byte[] audioByte = textToSpeechResponse.getResult().getOutput();
        Path path = Paths.get("output.mp3");
        Files.write(path, audioByte, StandardOpenOption.TRUNCATE_EXISTING);
        return "Audio file is created Successfully!. " + path.toAbsolutePath();
    }

}
