package pl.piomin.services.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.function.FunctionCallingOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final ChatClient chatClient;

    public WalletController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping
    String calculateWalletValue() {
        PromptTemplate pt = new PromptTemplate("""
        What’s the current value in dollars of my wallet based on the latest stock daily prices ?
        """);

        return this.chatClient.prompt(pt.create(
                FunctionCallingOptions.builder()
                        .function("numberOfShares")
                        .function("latestStockPrices")
                        .build()))
                .call()
                .content();
    }
}
