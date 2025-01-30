package pl.piomin.services.functions.wallet;

import java.util.List;
import java.util.function.Supplier;

public class WalletService implements Supplier<WalletResponse> {

    private WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletResponse get() {
        return new WalletResponse((List<Share>) walletRepository.findAll());
    }
}
