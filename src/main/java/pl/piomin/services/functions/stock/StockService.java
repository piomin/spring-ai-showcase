package pl.piomin.services.functions.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import pl.piomin.services.functions.stock.api.DailyStockData;
import pl.piomin.services.functions.stock.api.StockData;

import java.util.Map;
import java.util.function.Function;

public class StockService implements Function<StockRequest, StockResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(StockService.class);

    Map<String, Float> stockPrices = Map.of(
            "AAPL", 238.26F,
            "MSFT", 447.2F,
            "NVDA", 128.99F,
            "AMZN", 238.15F,
            "META", 674.33F
    );

    @Autowired
    RestTemplate restTemplate;
    @Value("${ALPHAVANTAGE_API_KEY}")
    String apiKey;

    @Override
    public StockResponse apply(StockRequest stockRequest) {
        LOG.info("Get stock prices: {}", stockRequest.company());
        StockData data = restTemplate.getForObject("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol={}&apikey={}",
                StockData.class,
                stockRequest.company(),
                apiKey);
        DailyStockData latestData = data.getTimeSeriesDaily().entrySet().stream()
                .findFirst()
                .orElseThrow()
                .getValue();
        return new StockResponse(Float.parseFloat(latestData.getClose()));
    }
}
