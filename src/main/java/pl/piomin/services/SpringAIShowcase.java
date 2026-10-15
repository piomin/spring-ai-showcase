package pl.piomin.services;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import pl.piomin.services.functions.stock.StockRequest;
import pl.piomin.services.functions.stock.StockResponse;
import pl.piomin.services.functions.stock.StockService;
import pl.piomin.services.functions.wallet.WalletRepository;
import pl.piomin.services.functions.wallet.WalletResponse;
import pl.piomin.services.functions.wallet.WalletService;
import pl.piomin.services.tools.StockTools;
import pl.piomin.services.tools.WalletTools;

@SpringBootApplication
public class SpringAIShowcase {

    public static void main(String[] args) {
        SpringApplication.run(SpringAIShowcase.class, args);
    }

    @Bean
    public ToolCallback numberOfShares(WalletRepository walletRepository) {
        return FunctionToolCallback.builder("numberOfShares", new WalletService(walletRepository))
                .description("Number of shares for each company in my portfolio")
                .inputType(Void.class)
                .build();
    }

    @Bean
    public ToolCallback latestStockPrices() {
        return FunctionToolCallback.builder("latestStockPrices", new StockService(restTemplate()))
                .description("Latest stock prices")
                .inputType(StockRequest.class)
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public StockTools stockTools() {
        return new StockTools(restTemplate());
    }

    @Bean
    public WalletTools walletTools(WalletRepository walletRepository) {
        return new WalletTools(walletRepository);
    }

    @Bean
    public ObservationRegistry observationRegistry() {
        return ObservationRegistry.create();
    }

//    @Bean
//    @ConditionalOnMissingBean(VectorStore.class)
//    VectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
//        return SimpleVectorStore.builder(embeddingModel).build();
//    }

//    @Bean
//    public BatchingStrategy customTokenCountBatchingStrategy() {
//        return new TokenCountBatchingStrategy(
//                EncodingType.CL100K_BASE,  // Specify the encoding type
//                8000,                      // Set the maximum input token count
//                0.1,// Set the reserve percentage
//                new MediaContentFormatter(),
//                MetadataMode.ALL
//        );
//    }

}
