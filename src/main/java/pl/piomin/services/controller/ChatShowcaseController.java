package pl.piomin.services.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatShowcaseController {

    private final ChatClient chatClient;

    public ChatShowcaseController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory))
                .build();
    }

    @RequestMapping("/{entity}")
    String dynamicApiAll(@PathVariable String entity) {
        PromptTemplate pt = new PromptTemplate("""
                Generate a list of {entity} with random values with basic info about them in separated fields.
                The number of fields can't be lower than 4 and higher than 7. Additionally each object in the list should contain an auto-incremented id field.
                Do not include any explanations or additional text.
                """);
        Prompt p = pt.create(Map.of("entity", entity));

        return this.chatClient.prompt(p)
                .call()
                .content();
    }

    @RequestMapping("/{entity}/{id}")
    String dynamicApiById(@PathVariable String entity, @PathVariable String id) {
        PromptTemplate pt = new PromptTemplate("""
                Find and return the object with id {id} in a current list of {entity}.
                """);
        Prompt p = pt.create(Map.of("entity", entity, "id", id));
        return this.chatClient.prompt(p)
                .call()
                .content();
    }
}
