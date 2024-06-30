package uz.ejavlon.quizbot.bot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@RequiredArgsConstructor
public class BotConfig {
    @Value("${telegrambot.userName}")
    String username;

    @Value("${telegrambot.webHookPath}")
    String botPath;

    @Value("${telegrambot.botToken}")
    String botToken;

    @Value("${zonaid}")
    String zonaId;

    private final Environment environment;

    @Bean
    public SetWebhook setWebhookInstance() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
                String.format(
                        environment.getProperty("telegrambot.setWebhookUrl"),
                        environment.getProperty("telegrambot.botToken"),
                        environment.getProperty("telegrambot.webHookPath")
                ),
                HttpMethod.GET, HttpEntity.EMPTY, Object.class
        );

        return SetWebhook.builder()
                .url(this.botPath)
                .build();
    }
}
