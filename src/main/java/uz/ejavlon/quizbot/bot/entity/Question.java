package uz.ejavlon.quizbot.bot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "_question")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(columnDefinition = "TEXT")
    @NotNull(message = "Description null")
    String description;

    Integer prizeCount;

    Double prizeAmount;

    String answer;

    @Temporal(TemporalType.DATE)
    LocalDate localDate;
}
