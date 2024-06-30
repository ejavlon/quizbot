package uz.ejavlon.quizbot.bot.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.ejavlon.quizbot.bot.enums.Status;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "_user_answer")
public class Answer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_answer",columnDefinition = "TEXT")
    String userAnswer;

    @Column(name = "user_answer_date_time")
    LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    Question question;

    @Enumerated(EnumType.STRING)
    Status status;
}
