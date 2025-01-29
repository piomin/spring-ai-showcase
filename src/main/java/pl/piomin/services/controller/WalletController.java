package pl.piomin.services.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piomin.services.model.Person;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final ChatClient chatClient;

    public WalletController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping
    String calculateWalletValue() {
        PromptTemplate pt = new PromptTemplate("""
        What’s the current value in $ of my wallet based on the latest stock daily prices ?
        """);

        return this.chatClient.prompt(pt.create(
                OpenAiChatOptions.builder()
                        .function("numberOfShares")
                        .function("latestStockPrices")
                        .build()))
                .call()
                .content();
    }
}
