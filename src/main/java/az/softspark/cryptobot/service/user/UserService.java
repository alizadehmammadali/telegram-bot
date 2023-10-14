package az.softspark.cryptobot.service.user;

import az.softspark.cryptobot.dao.entity.UserEntity;

import java.util.List;

public interface UserService {

    List<UserEntity> findAll();
    boolean subscribeUser(Long chatId);
    void unsubscribeUser(Long chatId);
}
