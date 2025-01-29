package pl.piomin.services;

import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.web.client.RestTemplate;
import pl.piomin.services.functions.stock.StockRequest;
import pl.piomin.services.functions.stock.StockResponse;
import pl.piomin.services.functions.stock.StockService;
import pl.piomin.services.functions.wallet.WalletResponse;
import pl.piomin.services.functions.wallet.WalletService;

import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class SpringAIShowcase {

    public static void main(String[] args) {
        SpringApplication.run(SpringAIShowcase.class, args);
    }

    @Bean
    InMemoryChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    @Description("Number of shares for each company in my portfolio")
    public Supplier<WalletResponse> numberOfShares() {
        return new WalletService();
    }

    @Bean
    @Description("Latest stock prices")
    public Function<StockRequest, StockResponse> latestStockPrices() {
        return new StockService();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
