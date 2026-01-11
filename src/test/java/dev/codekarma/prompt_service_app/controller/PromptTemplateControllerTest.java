package dev.codekarma.prompt_service_app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromptTemplateControllerTest {

    private ChatClient chatClient;
    private PromptTemplateController controller;

    @BeforeEach
    void setUp() {
        chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        controller = new PromptTemplateController(chatClient);

        when(chatClient.prompt(any(Prompt.class)).call().content())
                .thenReturn("mocked-response");

        // Clear recorded invocations so verifications only count calls from the tested method
        clearInvocations(chatClient);
    }

    @Test
    void findPopularSongsByArtist_shouldReturnMockedResponse_andCallChatClient() {
        String artist = "Arijit Singh";
        String result = controller.findPopularSongsByArtist(artist);

        assertEquals("mocked-response", result);
        verify(chatClient, times(1)).prompt(any(Prompt.class));

        ArgumentCaptor<Prompt> captor = ArgumentCaptor.forClass(Prompt.class);
        verify(chatClient).prompt(captor.capture());
        Prompt captured = captor.getValue();
        assertNotNull(captured);
    }

    @Test
    void findPopularSongsByArtistByResource_shouldReturnMockedResponse_andCallChatClient() throws Exception {
        // Provide a simple template resource and inject it into the private field
        Resource resource = new ByteArrayResource("List 10 songs for the {artist}".getBytes());
        Field f = PromptTemplateController.class.getDeclaredField("youtubePromptResource");
        f.setAccessible(true);
        f.set(controller, resource);

        String artist = "Arijit Singh";
        String result = controller.findPopularSongsByArtistByResource(artist);

        assertEquals("mocked-response", result);
        verify(chatClient, times(1)).prompt(any(Prompt.class));

        ArgumentCaptor<Prompt> captor = ArgumentCaptor.forClass(Prompt.class);
        verify(chatClient).prompt(captor.capture());
        Prompt captured = captor.getValue();
        assertNotNull(captured);
    }
}