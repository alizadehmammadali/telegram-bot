package az.softspark.cryptobot.client;

import az.softspark.cryptobot.model.Currency;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "crypto-currency-client", url = "${application.telegram-bot.client-url}")
public interface CryptoCurrencyClient {

    @GetMapping(value = "/ticker/price")
    List<Currency> getCryptoCurrencies();
}
