 package dev.codekarma.prompt_service_app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromptControllerTest {

    private ChatClient chatClient;
    private PromptController promptController;

    @BeforeEach
    void setUp() {
        chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        promptController = new PromptController(chatClient);

        when(chatClient.prompt(any(Prompt.class)).call().content())
                .thenReturn("mocked-response");

        clearInvocations(chatClient);
    }

    @Test
    void askMe_shouldReturnMockedResponse_andCallChatClient() {
        String result = promptController.askMe("Hello from test");

        assertEquals("mocked-response", result);
        verify(chatClient, times(1)).prompt(any(Prompt.class));
    }

    @Test
    void askMeByUserMessage_shouldReturnMockedResponse_andPassPrompt() {
        String message = "User message";
        String result = promptController.askMeByUserMessage(message);

        assertEquals("mocked-response", result);

        ArgumentCaptor<Prompt> captor = ArgumentCaptor.forClass(Prompt.class);
        verify(chatClient, times(1)).prompt(captor.capture());
        // Ensure a Prompt was passed to chatClient
        Prompt captured = captor.getValue();
        assertEquals(false, captured == null);
    }

    @Test
    void askMeBySystemMessage_shouldReturnMockedResponse_andCallChatClient() {
        String result = promptController.askMeBySystemMessage("System instruction");

        assertEquals("mocked-response", result);
        verify(chatClient, times(1)).prompt(any(Prompt.class));
    }
}
