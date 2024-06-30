package uz.ejavlon.quizbot.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.ejavlon.quizbot.base.dto.ResponseApi;
import uz.ejavlon.quizbot.bot.QuizBot;
import uz.ejavlon.quizbot.bot.config.BotConfig;
import uz.ejavlon.quizbot.bot.entity.Site;
import uz.ejavlon.quizbot.bot.entity.TelegramUser;
import uz.ejavlon.quizbot.bot.repository.TelegramUserRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final TelegramUserRepository userRepository;

    private final SiteService siteService;

    private final QuizBot quizBot;

    private final BotConfig botConfig;


    public ResponseApi getAllUsers(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber,10);
        return ResponseApi.builder()
                .message("All telegram users")
                .success(true)
                .data(userRepository.findAll(pageable))
                .build();
    }

    public ResponseApi deleteUser(Long id){
        if (userRepository.findById(id).isEmpty())
            return ResponseApi.builder()
                    .message("User not found")
                    .success(false)
                    .build();

        userRepository.deleteById(id);

        return ResponseApi.builder()
                .message("User successfully deleted")
                .success(true)
                .build();
    }

    public ResponseApi setBlockUser(Long id){
        Optional<TelegramUser> optionalTelegramUser = userRepository.findById(id);
        if (optionalTelegramUser.isEmpty())
            return ResponseApi.builder()
                    .message("User not found")
                    .success(false)
                    .build();

        TelegramUser telegramUser = optionalTelegramUser.get();
        telegramUser.setActive(false);
        userRepository.save(telegramUser);

        return ResponseApi.builder()
                .message("User successfully blocked")
                .success(true)
                .data(telegramUser)
                .build();
    }

    public ResponseApi setActiveUser(Long id){
        Optional<TelegramUser> optionalTelegramUser = userRepository.findById(id);
        if (optionalTelegramUser.isEmpty())
            return ResponseApi.builder()
                    .message("User not found")
                    .success(false)
                    .build();

        TelegramUser telegramUser = optionalTelegramUser.get();
        telegramUser.setActive(true);
        userRepository.save(telegramUser);

        return ResponseApi.builder()
                .message("User successfully activated")
                .success(true)
                .data(telegramUser)
                .build();
    }

    public boolean existTelegramUser(Message message){
        User user = message.getFrom();
        Optional<TelegramUser> optionalTelegramUser = userRepository.findByTelegramId(user.getId());
        if (optionalTelegramUser.isPresent())
            return true;

        TelegramUser telegramUser = TelegramUser.builder()
                .telegramId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUserName())
                .active(true)
                .joinedDate(LocalDate.now(ZoneId.of(botConfig.getZonaId())))
                .chatId(message.getChatId())
                .build();

        userRepository.save(telegramUser);
        return false;
    }

    public boolean checkSubscription(Long telegramId){
        List<Site> allSites = siteService.getAllSites();
        List<Site> telegramChannelOrGroups = allSites.stream().filter(Site::getIsTelegramGroupOrChannel).toList();
        boolean subscription = true;
        for (int i = 0; i < telegramChannelOrGroups.size(); i++) {
            String url = telegramChannelOrGroups.get(i).getUrl();
            String username = url.substring(url.indexOf("https://t.me/") + 13);
            if (!quizBot.isUserMemberOfChannel(telegramId,username)){
                subscription = false;
                break;
            }
        }
        return subscription;
    }

    public void saveQuestionId(Long telegramId, UUID questionId){
        Optional<TelegramUser> optionalTelegramUser = userRepository.findByTelegramId(telegramId);
        if (optionalTelegramUser.isEmpty())
            return;

        TelegramUser telegramUser = optionalTelegramUser.get();
        telegramUser.setLastQuestionId(questionId);
        userRepository.save(telegramUser);
    }
}
