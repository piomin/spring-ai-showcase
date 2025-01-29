package pl.piomin.services.functions.wallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

public class WalletService implements Supplier<WalletResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(WalletService.class);

    private WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletResponse get() {
        return new WalletResponse((List<Share>) walletRepository.findAll());
    }
}
