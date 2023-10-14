package az.softspark.cryptobot.util;

import az.softspark.cryptobot.dao.entity.CryptoCurrencyEntity;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static az.softspark.cryptobot.model.constants.Constants.FIRST_PRICE;
import static az.softspark.cryptobot.model.constants.Constants.LAST_PRICE;

public class CryptoCurrencyUtil {
    public static Map<String, String> getPrices(List<CryptoCurrencyEntity> cryptoCurrencyEntities) {
        if (cryptoCurrencyEntities == null || cryptoCurrencyEntities.isEmpty()) {
            return Map.of();
        }
        Comparator<CryptoCurrencyEntity> comparatorCreatedAt = Comparator.comparing(CryptoCurrencyEntity::getCreatedAt);
        CryptoCurrencyEntity firstPrice = cryptoCurrencyEntities.stream().min(comparatorCreatedAt).get();
        CryptoCurrencyEntity lastPrice = cryptoCurrencyEntities.stream().max(comparatorCreatedAt).get();
        return Map.of(FIRST_PRICE, firstPrice.getPrice(), LAST_PRICE, lastPrice.getPrice());
    }
}
