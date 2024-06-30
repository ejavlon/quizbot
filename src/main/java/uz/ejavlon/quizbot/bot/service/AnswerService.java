package uz.ejavlon.quizbot.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.ejavlon.quizbot.bot.entity.Answer;
import uz.ejavlon.quizbot.bot.entity.Question;
import uz.ejavlon.quizbot.bot.entity.TelegramUser;
import uz.ejavlon.quizbot.bot.enums.Status;
import uz.ejavlon.quizbot.bot.repository.AnswerRepository;
import uz.ejavlon.quizbot.bot.repository.QuestionRepository;
import uz.ejavlon.quizbot.bot.repository.TelegramUserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final TelegramUserRepository  telegramUserRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public Answer saveAnswer(long userId, String message) {
        Optional<TelegramUser> optional = telegramUserRepository.findByTelegramId(userId);
        if (optional.isEmpty()) return null;

        TelegramUser telegramUser = optional.get();
        UUID lastQuestionId = telegramUser.getLastQuestionId();
        List<Answer> answers = telegramUser.getAnswers();
        boolean exists = false;
        if (lastQuestionId != null)
            exists = answers.stream().anyMatch(answer -> answer.getQuestion().getId().equals(lastQuestionId));

        assert lastQuestionId != null;
        Question question = questionRepository.findById(lastQuestionId).get();

        Answer answer = Answer.builder()
                .dateTime(LocalDateTime.now(ZoneId.of("Asia/Tashkent")))
                .userAnswer(message)
                .question(question)
                .status(exists ? Status.REPEATED : message.trim().equalsIgnoreCase(question.getAnswer()) ? Status.RIGHT : Status.WRONG)
                .build();

        answer = answerRepository.save(answer);
        answers.add(answer);
        telegramUser.setAnswers(answers);
        telegramUserRepository.save(telegramUser);
        return answer;
    }

    public Answer getAnswer(long id) {
        return answerRepository.findById(id).orElse(null);
    }

    public List<Answer> getAnswers() {
        return answerRepository.findAll();
    }

}
