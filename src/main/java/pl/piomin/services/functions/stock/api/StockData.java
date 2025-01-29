package pl.piomin.services.functions.stock.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class StockData {

    @JsonProperty("Time Series (Daily)")
    private Map<String, DailyStockData> timeSeriesDaily;

    // Getters and setters
    public Map<String, DailyStockData> getTimeSeriesDaily() {
        return timeSeriesDaily;
    }

    public void setTimeSeriesDaily(Map<String, DailyStockData> timeSeriesDaily) {
        this.timeSeriesDaily = timeSeriesDaily;
    }
}
