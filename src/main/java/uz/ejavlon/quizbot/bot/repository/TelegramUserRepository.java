package uz.ejavlon.quizbot.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ejavlon.quizbot.bot.entity.TelegramUser;

import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser,Long> {
    Optional<TelegramUser> findByTelegramId(Long telegramId);
}
