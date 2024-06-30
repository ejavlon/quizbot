package uz.ejavlon.quizbot.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.ejavlon.quizbot.base.dto.ResponseApi;
import uz.ejavlon.quizbot.bot.QuizBot;
import uz.ejavlon.quizbot.bot.entity.Question;
import uz.ejavlon.quizbot.bot.entity.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UIService {

    private final SiteService siteService;


    public InlineKeyboardMarkup generateInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<Site> allSites = siteService.getAllSites();
        Site site = null;

        for (int i = 0; i < allSites.size(); i++) {
            site = allSites.get(i);
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setCallbackData(site.getUrl());
            inlineKeyboardButton.setText(allSites.get(i).getName());
            inlineKeyboardButton.setUrl(site.getUrl());
            row.add(inlineKeyboardButton);
            rows.add(row);
        }
        if (!rows.isEmpty()){
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("✅ Tasdiqlash");
            inlineKeyboardButton.setCallbackData("checkSubscription");
            row.add(inlineKeyboardButton);
            rows.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup sendAnswerMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton sendAnswer = new InlineKeyboardButton();
        sendAnswer.setCallbackData("sendAnswer");
        sendAnswer.setText("Javob yuborish ⚡\uFE0F");
        row.add(sendAnswer);
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public EditMessageText refreshInlineCourseButtons(CallbackQuery callbackQuery,String data){
        List<List<InlineKeyboardButton>> rowList = callbackQuery.getMessage().getReplyMarkup().getKeyboard();
        List<InlineKeyboardButton> navigation = rowList.get(rowList.size() - 1);
        navigation.get(0).setCallbackData(String.format("%s %s",data,UUID.randomUUID()));
        rowList.set(rowList.size()-1,navigation);
        callbackQuery.getMessage().getReplyMarkup().setKeyboard(rowList);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setReplyMarkup(callbackQuery.getMessage().getReplyMarkup());
        editMessageText.setText("<b>Bot'dan foydalanish uchun iltimos kanalga obuna bo'ling, keyin tasdiqlash tugmasini bosing!</b>");
        editMessageText.setParseMode("HTML");

        return editMessageText;
    }

}
