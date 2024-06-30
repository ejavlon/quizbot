package uz.ejavlon.quizbot.bot.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.ejavlon.quizbot.bot.QuizBot;
import uz.ejavlon.quizbot.bot.entity.Answer;
import uz.ejavlon.quizbot.bot.entity.Question;
import uz.ejavlon.quizbot.bot.enums.Status;
import uz.ejavlon.quizbot.bot.service.AnswerService;
import uz.ejavlon.quizbot.bot.service.QuestionService;
import uz.ejavlon.quizbot.bot.service.TelegramUserService;
import uz.ejavlon.quizbot.bot.service.UIService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/webhook")
@RequiredArgsConstructor
@Tag(name = "WebHook Controller")
public class WebHookController {

    private final QuizBot quizBot;
    private final UIService uiService;
    private final TelegramUserService telegramUserService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    private Boolean sendAnswer = false;

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        if (update.hasMessage()){
            handleMessage(update);
        }else if(update.hasCallbackQuery()){
            handleCallbackQuery(update);
        }
        return null;
    }

    public void handleMessage(Update update) {
        Message message = update.getMessage();
        if ("/start".equals(message.getText())){
            telegramUserService.existTelegramUser(update.getMessage());
            if(!telegramUserService.checkSubscription(message.getFrom().getId()))
                quizBot.sendMessage(update.getMessage().getChatId().toString(), "Bot'dan foydalanish uchun iltimos kanalga obuna bo'ling, keyin tasdiqlash tugmasini bosing!",uiService.generateInlineKeyboardMarkup());
        } else if (message.getText().contains("/start")) {
            UUID questionId = null;
            try {
                questionId = UUID.fromString(message.getText().substring(message.getText().indexOf("start") + 6));
                if (!telegramUserService.existTelegramUser(update.getMessage()) || !telegramUserService.checkSubscription(message.getFrom().getId())){
                    quizBot.sendMessage(update.getMessage().getChatId().toString(), "Bot'dan foydalanish uchun iltimos kanalga obuna bo'ling, keyin tasdiqlash tugmasini bosing!",uiService.generateInlineKeyboardMarkup());
                    telegramUserService.saveQuestionId(message.getFrom().getId(),questionId);
                    return;
                }
            }catch (IllegalArgumentException exception){
                quizBot.sendMessage(message.getChatId().toString(),"Bunday savol mavjud emas!");
                return;
            }
            telegramUserService.saveQuestionId(message.getFrom().getId(),questionId);
            Question questionByID = questionService.getQuestionByID(questionId);
            if (questionByID == null){
                quizBot.sendMessage(message.getChatId().toString(),"Bunday savol mavjud emas!");
                return;
            }

            quizBot.sendMessage(message.getChatId().toString(), questionByID.getDescription(), uiService.sendAnswerMarkup());
        } else if (this.sendAnswer && !"".equals(message.getText())) {
            System.out.println("message.getText() = " + message.getText());

            Answer answer = answerService.saveAnswer(message.getFrom().getId(), message.getText());
            if (answer != null && answer.getStatus() == Status.RIGHT){
                quizBot.sendMessage(message.getChatId().toString(),"Tabriklaymiz javobingiz tog'ri!");
            }else if (answer != null && answer.getStatus() == Status.WRONG){
                quizBot.sendMessage(message.getChatId().toString(),"Afsuski javobingiz notog'ri!");
            } else if (answer != null && answer.getStatus() == Status.REPEATED) {
                quizBot.sendMessage(message.getChatId().toString(),"Bu savolga javob berib bo'lgansiz!");
            }

            System.out.println("answer = " + answer);
            this.sendAnswer = false;
        }
    }

    public void handleCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if ("checkSubscription".contains(callbackQuery.getData())) {
            if (telegramUserService.checkSubscription(callbackQuery.getFrom().getId())){
                Question question = questionService.getLastQuestionByTelegramId(callbackQuery.getFrom().getId());
                if (question == null) return;
                new Thread(() -> {
                    quizBot.editMessageText(uiService.refreshInlineCourseButtons(callbackQuery,"checkSubscription"));
                }).start();
                quizBot.sendMessage(callbackQuery.getMessage().getChatId().toString(), question.getDescription(), uiService.sendAnswerMarkup());
                return;
            }
            new Thread(() -> {
                quizBot.editMessageText(uiService.refreshInlineCourseButtons(callbackQuery,"checkSubscription"));
            }).start();
            quizBot.sendMessage(callbackQuery.getMessage().getChatId().toString(), "Bot'dan foydalanish uchun iltimos kanalga obuna bo'ling, keyin tasdiqlash tugmasini bosing!",uiService.generateInlineKeyboardMarkup());
        } else if ("sendAnswer".equals(callbackQuery.getData())) {
            quizBot.getAnswerEditMessage(callbackQuery.getMessage().getChatId(),callbackQuery.getMessage().getMessageId(), "Javob matnini kiriting: ");
            this.sendAnswer = true;
            return;
        }

    }
}
