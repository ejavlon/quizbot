package uz.ejavlon.quizbot.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.ejavlon.quizbot.base.dto.ResponseApi;
import uz.ejavlon.quizbot.bot.config.BotConfig;
import uz.ejavlon.quizbot.bot.entity.Question;
import uz.ejavlon.quizbot.bot.entity.TelegramUser;
import uz.ejavlon.quizbot.bot.repository.QuestionRepository;
import uz.ejavlon.quizbot.bot.repository.TelegramUserRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final BotConfig botConfig;

    private final QuestionRepository questionRepository;

    private final TelegramUserRepository telegramUserRepository;

    public ResponseApi getAllQuestions(int page){
        Pageable pageable = PageRequest.of(page,10);
        return ResponseApi.builder()
                .message("All Questions")
                .success(true)
                .data(questionRepository.findAll(pageable))
                .build();
    }

    public Question getQuestionByID(UUID id){
        return questionRepository.findById(id).orElse(null);
    }

    public ResponseApi getQuestionById(UUID id){
        Optional<Question> optionalQuestion = questionRepository.findById(id);

        if (optionalQuestion.isEmpty())
            return ResponseApi.builder()
                    .message("Question not found")
                    .success(false)
                    .build();

        return ResponseApi.builder()
                .data(optionalQuestion.get())
                .message("Question found")
                .success(true)
                .build();
    }

    public ResponseApi addQuestion(Question question){
        if(questionRepository.existsByDescription(question.getDescription()))
            return ResponseApi.builder()
                    .message("There is such a question")
                    .success(false)
                    .build();

        question.setLocalDate(LocalDate.now(ZoneId.of(botConfig.getZonaId())));
        question = questionRepository.save(question);

        return ResponseApi.builder()
                .message("Question successfully saved")
                .success(true)
                .data(question)
                .build();
    }

    @Transactional
    public ResponseApi updateQuestion(UUID id, Question question){
        Optional<Question> optionalQuestion = questionRepository.findById(id);

        if (optionalQuestion.isEmpty())
            return ResponseApi.builder()
                    .message("Question not found")
                    .success(false)
                    .build();

        Question updatedQuestion = optionalQuestion.get();
        updatedQuestion.setDescription(question.getDescription());
        updatedQuestion.setPrizeCount(question.getPrizeCount());
        updatedQuestion.setPrizeAmount(question.getPrizeAmount());
        updatedQuestion.setAnswer(question.getAnswer());
        updatedQuestion.setLocalDate(LocalDate.now(ZoneId.of(botConfig.getZonaId())));
        updatedQuestion = questionRepository.save(updatedQuestion);

        return ResponseApi.builder()
                .message("Question successfully updated")
                .success(true)
                .data(updatedQuestion)
                .build();
    }

    public ResponseApi deleteQuestion(UUID id){
        if (questionRepository.findById(id).isEmpty())
            return ResponseApi.builder()
                    .message("Question not found")
                    .success(false)
                    .build();

        questionRepository.deleteById(id);
        return ResponseApi.builder()
                .message("Question successfully deleted")
                .success(true)
                .build();
    }


    @Transactional
    public Question getLastQuestionByTelegramId(Long telegramId){
        Optional<TelegramUser> optionalTelegramUser = telegramUserRepository.findByTelegramId(telegramId);
        TelegramUser telegramUser = optionalTelegramUser.orElse(null);
        if (telegramUser == null) return null;

        return questionRepository.findById(telegramUser.getLastQuestionId()).orElse(null);
    }
}
