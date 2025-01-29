package pl.piomin.services.functions.wallet;

import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Share, Long> {
}
