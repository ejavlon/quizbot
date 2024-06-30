package uz.ejavlon.quizbot.bot;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import uz.ejavlon.quizbot.bot.config.BotConfig;

@Component
public class QuizBot extends SpringWebhookBot {
    private final BotConfig botConfig;

    public QuizBot(BotConfig botConfig) {
        super(botConfig.setWebhookInstance(), botConfig.getBotToken());
        this.botConfig = botConfig;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return this.botConfig.getBotPath();
    }

    @Override
    public String getBotUsername() {
        return this.botConfig.getUsername();
    }

    @SneakyThrows
    public void sendMessage(String chatId, String message, InlineKeyboardMarkup markup){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(markup);
        execute(sendMessage);
    }

    @SneakyThrows
    public void sendMessage(String chatId, String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        execute(sendMessage);
    }

    @SneakyThrows
    public void getAnswerEditMessage(Long chatId,Integer messageId, String answer) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setText(answer);
        editMessageText.setChatId(chatId);
        execute(editMessageText);
    }

    @SneakyThrows
    public void editMessageText(EditMessageText editMessageText) {
        execute(editMessageText);
    }


    @SneakyThrows
    public boolean isUserMemberOfChannel(Long userId, String channelUsername) {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId("@" + channelUsername);
        getChatMember.setUserId(userId);
        ChatMember chatMember = execute(getChatMember);
        String status = chatMember.getStatus();
        return !status.equals("left") && !status.equals("kicked");
    }
}
