package az.softspark.cryptobot.service.user;

import az.softspark.cryptobot.bot.PhototecaCryptoCurrencyBot;
import az.softspark.cryptobot.dao.entity.CryptoCurrencyEntity;
import az.softspark.cryptobot.dao.entity.UserEntity;
import az.softspark.cryptobot.service.currency.CryptoCurrencyService;
import az.softspark.cryptobot.util.CryptoCurrencyUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static az.softspark.cryptobot.model.constants.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserNotificationServiceImpl implements UserNotificationService {

    private static final String STATUS_PATTERN = "${status}";
    private static final String INCREASED = "increased";
    private static final String DECREASED = "decreased";

    @Value("${application.telegram-bot.percentage-change}")
    private Double percentageChange;

    private final UserService userService;
    private final CryptoCurrencyService cryptoCurrencyService;
    private final PhototecaCryptoCurrencyBot phototecaCryptoCurrencyBot;
    private final Set<String> notificationKeys = new HashSet<>();

    @SneakyThrows
    @Override
    public void checkNotification() {
        log.info("Sending notification to users if there is any change on specific crypto currency");
        List<UserEntity> users = userService.findAll();
        for (UserEntity user : users) {
            LocalDateTime userSubscriptionCreatedAt = user.getCreatedAt();
            Optional<List<CryptoCurrencyEntity>> currenciesByUser = cryptoCurrencyService.getAllByCreatedAtAfter(userSubscriptionCreatedAt);
            if (currenciesByUser.isPresent()) {
                Map<String, List<CryptoCurrencyEntity>> groupedBySymbol = currenciesByUser.get()
                        .stream()
                        .collect(Collectors.groupingBy(CryptoCurrencyEntity::getSymbol)); //Assume user subscribed all cryptoCurrencies
                for (Map.Entry<String, List<CryptoCurrencyEntity>> currenciesBySymbol : groupedBySymbol.entrySet()) {
                    processNotification(currenciesBySymbol.getValue(), user, currenciesBySymbol.getKey());
                }
            }
        }
    }

    @SneakyThrows
    @Override
    public void processNotification(List<CryptoCurrencyEntity> currenciesBySymbol, UserEntity user, String symbol) {
        Map<String, String> prices = CryptoCurrencyUtil.getPrices(currenciesBySymbol);
        if (!prices.isEmpty()) {
            BigDecimal firstPrice = new BigDecimal(prices.get(FIRST_PRICE));
            BigDecimal lastPrice = new BigDecimal(prices.get(LAST_PRICE));
            BigDecimal currentPriceIncreased = firstPrice.multiply(BigDecimal.valueOf((1 + (percentageChange / 100))));
            BigDecimal currentPriceDecreased = firstPrice.multiply(BigDecimal.valueOf((1 - (percentageChange / 100))));
            String message = "%s crypto price has been ${status} from %s price to %s since you subscribed  %s"
                    .formatted(symbol, firstPrice.toPlainString(), lastPrice.toPlainString(), FORMATTER.format(user.getCreatedAt()));
            if (lastPrice.compareTo(currentPriceIncreased) > 0) {
                message = message.replace(STATUS_PATTERN, INCREASED);
            } else if (firstPrice.compareTo(currentPriceDecreased) < 0) {
                message = message.replace(STATUS_PATTERN, DECREASED);
            }
            if (!message.contains(STATUS_PATTERN)) { //Check if there is message
                log.info(message);
                //TODO We can store sending message as chatID_firstPrice.toPlainString()_lastPrice.toPlainString() as key and check before sending it
                String key = user.getChatId() + "_" + firstPrice.toPlainString() + "_" + lastPrice.toPlainString();
                if (!notificationKeys.contains(key)) {
                    phototecaCryptoCurrencyBot.sendNotification(user.getChatId().toString(), message);
                    notificationKeys.add(key);
                }
            }
        }
    }

}
