package dev.codekarma.prompt_service_app.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromptController {

    private final ChatClient chatClient;

    public PromptController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    @GetMapping("/ask-me")
    public String askMe(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message){
        Prompt prompt = new Prompt(message);

        return chatClient.
                prompt(prompt)
                .call()
                .content();
    }

    @GetMapping("/ask-me-by-user-message")
    public String askMeByUserMessage(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message){
        UserMessage userMessage = new UserMessage(message);

        Prompt prompt = new Prompt(userMessage);

        return chatClient.
                prompt(prompt)
                .call()
                .content();
    }


    @GetMapping("/ask-me-by-system-message")
    public String askMeBySystemMessage(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message){

        var systemMessage = new SystemMessage(message);
        Prompt prompt = new Prompt(systemMessage);
        return chatClient.
                prompt(prompt)
                .call()
                .content();
    }

}
