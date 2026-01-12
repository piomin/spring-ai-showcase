package pl.piomin.services.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.StructuredOutputValidationAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piomin.services.model.Person;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final ChatClient chatClient;

    public PersonController(ChatClient.Builder chatClientBuilder,
                            ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping
    List<Person> findAll() {
        PromptTemplate pt = new PromptTemplate("""
                Return a current list of 10 persons if exists or generate a new list with random values.
                Each object should contain an auto-incremented id field.
                The age value should be a random number between 18 and 99.
                Do not include any explanations or additional text.
                Return data in RFC8259 compliant JSON format.
                """);

        var type = new ParameterizedTypeReference<List<Person>>() {};
        return this.chatClient.prompt(pt.create())
                .advisors(StructuredOutputValidationAdvisor.builder()
                        .outputType(type)
                        .build())
                .call()
                .entity(type);
    }

    @GetMapping("/{id}")
    Person findById(@PathVariable String id) {
        PromptTemplate pt = new PromptTemplate("""
                Find and return the object with id {id} in a current list of persons.
                """);
        Prompt p = pt.create(Map.of("id", id));
        return this.chatClient.prompt(p)
                .advisors(StructuredOutputValidationAdvisor.builder()
                        .outputType(Person.class)
                        .build())
                .call()
                .entity(Person.class);
    }
}
