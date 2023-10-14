package az.softspark.cryptobot.dao.repository;

import az.softspark.cryptobot.dao.entity.CryptoCurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrencyEntity, Long> {
    Optional<List<CryptoCurrencyEntity>> getCryptoCurrencyEntitiesByCreatedAtAfter(LocalDateTime createdAt);
}
