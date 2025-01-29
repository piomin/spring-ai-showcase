package pl.piomin.services.functions.wallet;

import java.util.Map;

public record WalletResponse(Map<String, Integer> shares) {
}
