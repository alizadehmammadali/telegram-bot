package az.softspark.cryptobot.scheduler;

import az.softspark.cryptobot.service.CryptoPriceCheckerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoPriceScheduler {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final CryptoPriceCheckerService cryptoPriceCheckerService;

    @Scheduled(fixedRate = 3600)
    public void checkCryptoPricesPeriodically() {
        log.info("Scheduler has been started at:" + formatter.format(LocalDateTime.now()));
        cryptoPriceCheckerService.checkCryptoPrices();
    }
}
