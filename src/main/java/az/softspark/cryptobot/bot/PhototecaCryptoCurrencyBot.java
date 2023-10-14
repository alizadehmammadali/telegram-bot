package az.softspark.cryptobot.bot;

import az.softspark.cryptobot.service.currency.CryptoCurrencyService;
import az.softspark.cryptobot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class PhototecaCryptoCurrencyBot extends TelegramLongPollingBot {

    @Value("${application.telegram-bot.username}")
    private String botUsername;

    @Value("${application.telegram-bot.token}")
    private String botToken;

    private final UserService userService;

    private final CryptoCurrencyService cryptoCurrencyService;

    private static final String RESTART_MESSAGE = "ALL DATA HAS BEEN RESETTED";
    private static final String BOT_IS_NOT_AVAILABLE = "Bot is not available.It reached to maximum user";

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if ("/start".equals(messageText)) {
                boolean isSubscribed = userService.subscribeUser(chatId);
                if (!isSubscribed) {
                    sendNotification(chatId.toString(), BOT_IS_NOT_AVAILABLE);
                }
            } else if ("/stop".equals(messageText)) {
                userService.unsubscribeUser(chatId);
            } else if ("/restart".equals(messageText)) {
                userService.unsubscribeUser(chatId);
                cryptoCurrencyService.deleteAll();
                sendNotification(chatId.toString(), RESTART_MESSAGE);
            }
        }
    }


    public void sendNotification(String chatId, String msg) throws TelegramApiException {
        var response = new SendMessage(chatId, msg);
        execute(response);
    }
}
