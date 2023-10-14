package az.softspark.cryptobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "az.softspark.cryptobot.*",
        "org.telegram.telegrambots"
})
@EnableFeignClients(basePackages = "az.softspark.cryptobot.client")
public class CryptoBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoBotApplication.class, args);
    }

}
