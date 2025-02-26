package pl.piomin.services.functions.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import pl.piomin.services.functions.stock.api.DailyStockData;
import pl.piomin.services.functions.stock.api.StockData;

import java.util.function.Function;

public class StockService implements Function<StockRequest, StockResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(StockService.class);

    @Autowired
    RestTemplate restTemplate;
    @Value("${STOCK_API_KEY:none}")
    String apiKey;

    @Override
    public StockResponse apply(StockRequest stockRequest) {
        StockData data = restTemplate.getForObject("https://api.twelvedata.com/time_series?symbol={0}&interval=1min&outputsize=1&apikey={1}",
                StockData.class,
                stockRequest.company(),
                apiKey);
        DailyStockData latestData = data.getValues().get(0);
        LOG.info("Get stock prices: {} -> {}", stockRequest.company(), latestData.getClose());
        return new StockResponse(Float.parseFloat(latestData.getClose()));
    }
}
