package com.example.TextToSpeech.Controller;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Controller
@RequestMapping
public class ImageController {
    private final ImageModel imageModel;

    ImageController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping("/image")
    public ImageResponse imageGen(@RequestParam("message") String message) {
        return imageModel.call(new ImagePrompt(message));
    }

    @GetMapping("/custom-image")
    public String imageGen2(@RequestParam("message") String message) {
        Path path = Paths.get("image.png");
        String imageUrl = imageModel.call(new ImagePrompt(message)).getResult().getOutput().getUrl();
        return imageUrl;
    }

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

    @GetMapping("/custom-image3")
    public String generateImageWithWebpage(Model model) throws IOException {

        String b64Json = imageModel.call(new ImagePrompt("sunset over yokohama",
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
        Loader(model, b64Json);
        return "main";
    }
    // Pass the string to the HTML
    // model.addAttribute("myImage", b64Json);
    public void Loader(Model model, String b64JsonImage) {
        model.addAttribute("myImage", b64JsonImage);
    }

}
