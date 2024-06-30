package uz.ejavlon.quizbot.bot.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ejavlon.quizbot.bot.entity.Question;

import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    boolean existsByDescription(@NotNull(message = "Description null") String description);
}
