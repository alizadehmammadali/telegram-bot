package az.softspark.cryptobot.service.currency;

import az.softspark.cryptobot.dao.entity.CryptoCurrencyEntity;
import az.softspark.cryptobot.dao.repository.CryptoCurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {

    private final CryptoCurrencyRepository cryptoCurrencyRepository;

    @Override
    public void deleteAll() {
        cryptoCurrencyRepository.deleteAll();
    }

    @Override
    public Optional<List<CryptoCurrencyEntity>> getAllByCreatedAtAfter(LocalDateTime createdAt) {
        return cryptoCurrencyRepository
                .getCryptoCurrencyEntitiesByCreatedAtAfter(createdAt);
    }
}
