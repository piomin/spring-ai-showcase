package pl.piomin.services.functions.stock.api;

import java.util.List;

public class Stock {

    private String name;
    private String symbol;
    private List<String> prices;

    public Stock() {
    }

    public Stock(String name, List<String> prices) {
        this.prices = prices;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<String> getPrices() {
        return prices;
    }

    public void setPrices(List<String> prices) {
        this.prices = prices;
    }
}
