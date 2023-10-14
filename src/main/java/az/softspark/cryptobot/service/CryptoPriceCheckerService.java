package az.softspark.cryptobot.service;

import az.softspark.cryptobot.client.CryptoCurrencyClient;
import az.softspark.cryptobot.dao.entity.CryptoCurrencyEntity;
import az.softspark.cryptobot.dao.repository.CryptoCurrencyRepository;
import az.softspark.cryptobot.model.Currency;
import az.softspark.cryptobot.service.user.UserNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoPriceCheckerService {

    private final CryptoCurrencyClient cryptoCurrencyClient;

    private final CryptoCurrencyRepository cryptoCurrencyRepository;

    private final UserNotificationService userNotificationService;

    public void checkCryptoPrices() {
        log.info("Checking new crypto prices from API");
        List<Currency> cryptoCurrencies = cryptoCurrencyClient.getCryptoCurrencies();
        List<CryptoCurrencyEntity> newCryptoCurrencies = cryptoCurrencies.stream().map(currency -> {
            CryptoCurrencyEntity entity = new CryptoCurrencyEntity();
            entity.setSymbol(currency.getSymbol());
            entity.setPrice(currency.getPrice());
            entity.setCreatedAt(LocalDateTime.now());
            return entity;
        }).toList();
        cryptoCurrencyRepository.saveAll(newCryptoCurrencies);
        log.info("Saved all new crypto prices");
        userNotificationService.checkNotification();
    }

}
