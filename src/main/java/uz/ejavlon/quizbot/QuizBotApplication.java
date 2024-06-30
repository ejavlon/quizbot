package uz.ejavlon.quizbot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.ejavlon.quizbot.base.dto.SignInDto;
import uz.ejavlon.quizbot.base.entity.User;
import uz.ejavlon.quizbot.base.enums.Role;
import uz.ejavlon.quizbot.base.repository.UserRepository;
import uz.ejavlon.quizbot.base.service.UserService;

import java.util.Optional;

@SpringBootApplication
@RequiredArgsConstructor
public class QuizBotApplication {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;


    public static void main(String[] args) {
        SpringApplication.run(QuizBotApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(Environment environment){
        String initMode = environment.getProperty("spring.sql.init.mode");
        return args -> {
            if ("always".equals(initMode)){
                var admin = User.builder()
                        .firstName("Javlon")
                        .lastName("Ergashev")
                        .username("root")
                        .password(passwordEncoder.encode("root123"))
                        .role(Role.SUPER_ADMIN)
                        .build();

                Optional<User> optionalUser = userRepository.findByUsername(admin.getUsername());
                if (optionalUser.isEmpty()){
                    userRepository.save(admin);
                }
                userService.signIn(SignInDto.builder().username(admin.getUsername()).password("root123").build());
                System.out.println("Admin (ejavlon) signin system");
            }
        };
    }
}
