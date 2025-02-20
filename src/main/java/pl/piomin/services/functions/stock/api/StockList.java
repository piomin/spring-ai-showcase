package pl.piomin.services.functions.stock.api;

import java.util.List;

public class StockList {

    private List<Stock> stocks;

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
