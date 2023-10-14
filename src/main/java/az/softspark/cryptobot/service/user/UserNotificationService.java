package az.softspark.cryptobot.service.user;

import az.softspark.cryptobot.dao.entity.CryptoCurrencyEntity;
import az.softspark.cryptobot.dao.entity.UserEntity;

import java.util.List;

public interface UserNotificationService {
    void checkNotification();

    void processNotification(List<CryptoCurrencyEntity> currenciesBySymbol, UserEntity userEntity, String symbol);
}
