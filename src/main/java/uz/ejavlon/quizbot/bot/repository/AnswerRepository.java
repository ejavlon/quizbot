package uz.ejavlon.quizbot.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ejavlon.quizbot.bot.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
