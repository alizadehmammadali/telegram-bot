package az.softspark.cryptobot.service.user;

import az.softspark.cryptobot.bot.PhototecaCryptoCurrencyBot;
import az.softspark.cryptobot.dao.entity.UserEntity;
import az.softspark.cryptobot.dao.repository.UserRepository;
import az.softspark.cryptobot.service.currency.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${application.telegram-bot.max-user-count}")
    private Integer maxUserCount;
    private final UserRepository userRepository;
    private final CryptoCurrencyService cryptoCurrencyService;

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @SneakyThrows
    @Override
    public boolean subscribeUser(Long chatId) {
        Optional<UserEntity> user = userRepository.findByChatId(chatId);
        if (user.isEmpty()) {
            if (userRepository.count() >= maxUserCount) {
                return false;
            }
            UserEntity newUser = new UserEntity();
            newUser.setChatId(chatId);
            newUser.setCreatedAt(LocalDateTime.now());
            userRepository.save(newUser);
        }
        return true;
    }

    @Override
    public void unsubscribeUser(Long chatId) {
        Optional<UserEntity> user = userRepository.findByChatId(chatId);
        user.ifPresent(userRepository::delete);
    }
}
