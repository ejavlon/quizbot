package uz.ejavlon.quizbot.bot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "_telegram_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "telegram_id", nullable = false,unique = true)
    Long telegramId;

    @Column(name = "last_question_id")
    UUID lastQuestionId;

    @Column(name = "first_name",length = 50)
    String firstName;

    @Column(name = "last_name",length = 50)
    String lastName;

    @Column(name = "username",length = 50,unique = true)
    String username;

    @Column(name = "chat_id")
    Long chatId;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    @Column(columnDefinition = "boolean default true")
    Boolean active = true;

    @Temporal(TemporalType.DATE)
    LocalDate joinedDate;

    @ManyToMany(fetch = FetchType.LAZY)
//            @JoinTable(
//                    name = "user_answer",
//                    joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
//                    inverseJoinColumns = {@JoinColumn(name = "answer_id",referencedColumnName = "id")}
//            )
    List<Answer> answers;
}
