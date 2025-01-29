package pl.piomin.services.functions.stock.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StockData {

    private List<DailyStockData> values;

    public List<DailyStockData> getValues() {
        return values;
    }

    public void setValues(List<DailyStockData> values) {
        this.values = values;
    }
}
