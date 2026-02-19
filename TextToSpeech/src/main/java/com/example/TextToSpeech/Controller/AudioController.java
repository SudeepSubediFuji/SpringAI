package com.example.TextToSpeech.Controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AudioController {
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    AudioController(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel){
        this.openAiAudioTranscriptionModel=openAiAudioTranscriptionModel;
    }

    @GetMapping("/audio")
    String transcibe(@Value("classpath:Audio/OpenAI.mp3")Resource Audio){
        return openAiAudioTranscriptionModel.call(Audio);
    }
    // AudioTranscriptionPrompt
    @GetMapping("/audio-options")
    String transcibe2(@Value("classpath:Audio/OpenAI.mp3")Resource Audio){
       var audioTranscriptResponse =  openAiAudioTranscriptionModel.call(
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
    String transcibe3(@Value("classpath:Audio/JPOpenAI.mp3")Resource Audio){
        var audioTranscriptResponse =  openAiAudioTranscriptionModel.call(
                new AudioTranscriptionPrompt(Audio,
                        OpenAiAudioTranscriptionOptions.builder()
                                .prompt("Talking about OpenAI").language("ja")
                                .temperature(0.5f)
                                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.VTT)
                                .build()));
        return audioTranscriptResponse.getResult().getOutput();
    }

}
