package pl.piomin.services.functions.wallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Supplier;

public class WalletService implements Supplier<WalletResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(WalletService.class);

    Map<String, Integer> stockQuantities = Map.of(
            "AAPL", 200,
            "MSFT", 400,
            "NVDA", 100,
            "AMZN", 600,
            "META", 900
    );

    @Override
    public WalletResponse get() {
        return new WalletResponse(stockQuantities);
    }
}
