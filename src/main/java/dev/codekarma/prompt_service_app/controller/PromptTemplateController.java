package dev.codekarma.prompt_service_app.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PromptTemplateController {

    private final ChatClient chatClient;

    @Value("classpath:prompts/youtube-songs.st")
    private Resource youtubePromptResource;

    public PromptTemplateController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/youtube")
    public String findPopularSongsByArtist(@RequestParam(value = "artist", defaultValue = "Arijit Singh") String artist){

        String message = """
                        List 10 of the the most played Youtube songs along with their genre for the {artist}.
                        If you don't know the answer, just say "I don't know".\s
                       \s""";

        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("artist",artist));

        return chatClient
                .prompt(prompt)
                .call()
                .content();

    }

    @GetMapping("/youtube-search")
    public String findPopularSongsByArtistByResource(@RequestParam(value = "artist", defaultValue = "Arijit Singh") String artist){


        PromptTemplate promptTemplate = new PromptTemplate(youtubePromptResource);
        Prompt prompt = promptTemplate.create(Map.of("artist",artist));

        return chatClient
                .prompt(prompt)
                .call()
                .content();

    }

}
