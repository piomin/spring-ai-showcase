package pl.piomin.services.tools;

import org.springframework.ai.tool.annotation.Tool;
import pl.piomin.services.functions.wallet.Share;
import pl.piomin.services.functions.wallet.WalletRepository;

import java.util.List;

public class WalletTools {

    private WalletRepository walletRepository;

    public WalletTools(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Tool(description = "Number of shares for each company in my portfolio")
    public List<Share> getNumberOfShares() {
        return (List<Share>) walletRepository.findAll();
    }
}
