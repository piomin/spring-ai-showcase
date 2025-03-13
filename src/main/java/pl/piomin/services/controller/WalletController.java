package pl.piomin.services.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.function.FunctionCallingOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piomin.services.tools.StockTools;
import pl.piomin.services.tools.WalletTools;

import java.util.Map;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final ChatClient chatClient;
    private final StockTools stockTools;
    private final WalletTools walletTools;

    public WalletController(ChatClient.Builder chatClientBuilder,
                            StockTools stockTools,
                            WalletTools walletTools) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.stockTools = stockTools;
        this.walletTools = walletTools;
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

    @GetMapping("/with-tools")
    String calculateWalletValueWithTools() {
        PromptTemplate pt = new PromptTemplate("""
        What’s the current value in dollars of my wallet based on the latest stock daily prices ?
        """);

        return this.chatClient.prompt(pt.create())
                .tools(stockTools, walletTools)
                .call()
                .content();
    }

    @GetMapping("/highest-day/{days}")
    String calculateHighestWalletValue(@PathVariable int days) {
        PromptTemplate pt = new PromptTemplate("""
        On which day during last {days} days my wallet had the highest value in dollars based on the historical daily stock prices ?
        """);

        return this.chatClient.prompt(pt.create(Map.of("days", days)))
                .tools(stockTools, walletTools)
                .call()
                .content();
    }
}
