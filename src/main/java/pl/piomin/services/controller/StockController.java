package pl.piomin.services.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.reader.EmptyJsonMetadataGenerator;
import org.springframework.ai.reader.JsonMetadataGenerator;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.piomin.services.functions.stock.api.DailyStockData;
import pl.piomin.services.functions.stock.api.StockData;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final static Logger LOG = LoggerFactory.getLogger(StockController.class);
    private final ChatClient chatClient;

    private VectorStore store;

    public StockController(ChatClient.Builder chatClientBuilder,
                           VectorStore store) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.store = store;
    }

    @Autowired
    RestTemplate restTemplate;
    @Value("${STOCK_API_KEY}")
    String apiKey;

    @GetMapping("/load")
    void load() {
        final List<String> companies = List.of("AAPL", "MSFT", "GOOG", "AMZN", "META", "NVDA");
        for (String company : companies) {
            StockData data = restTemplate.getForObject("https://api.twelvedata.com/time_series?symbol={0}&interval=1day&outputsize=10&apikey={1}",
                    StockData.class,
                    company,
                    apiKey);
            if (data != null && data.getValues() != null) {
                var list = data.getValues().stream().map(DailyStockData::getClose).toList();
                var doc = Document.builder()
                        .id(company)
                        .text(list.toString())
                        .build();
                store.add(List.of(doc));
                LOG.info("Document added: {}", company);
            }
//            var list = data.getValues().stream().map(DailyStockData::getClose).toList();
//            Document.builder().id(company).text(list.toString());
//            if (jsonResp != null) {
//                Resource jsonResource = new ByteArrayResource(jsonResp.getBytes(StandardCharsets.UTF_8));
//                List<Document> documents = new JsonReader(jsonResource, "values.close").get();
//                store.add(documents);
//            }
        }
    }

    @GetMapping("/docs")
    List<Document> query() {
        SearchRequest searchRequest = SearchRequest.builder()
                .query("Similar trend to: [103.7, 100.4, 101.3, 102.8, 101.3, 98.6, 98.5, 100.8, 99.8, 100.0]")
                .topK(2)
                .build();
        List<Document> docs = store.similaritySearch(searchRequest);

        return docs;
    }

    @RequestMapping("/find-best-trend")
    String getBestTrend() {
        PromptTemplate pt = new PromptTemplate("""
                Which of those market price trends is the most growth?
                The first element in the list is the latest. 
                """);

        return this.chatClient.prompt(pt.create())
                .advisors(new QuestionAnswerAdvisor(store))
                .call()
                .content();
    }

    @RequestMapping("/v2/find-best-trend")
    String getBestTrendV2() {
        PromptTemplate pt = new PromptTemplate("""
                Which of those market price trends is the most growth?
                The first element in the list is the latest. 
                """);

        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.5)
                        .vectorStore(store)
                        .build())
                .build();

        return this.chatClient.prompt(pt.create())
                .advisors(retrievalAugmentationAdvisor)
                .call()
                .content();
    }

}
