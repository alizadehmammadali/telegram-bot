package az.softspark.cryptobot.service.currency;

import az.softspark.cryptobot.dao.entity.CryptoCurrencyEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CryptoCurrencyService {
    void deleteAll();
    Optional<List<CryptoCurrencyEntity>> getAllByCreatedAtAfter(LocalDateTime createdAt);
}
