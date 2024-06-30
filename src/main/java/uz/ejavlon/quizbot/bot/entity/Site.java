package uz.ejavlon.quizbot.bot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "_sites")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Site implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(length = 60)
    @NotNull(message = "name null")
    String name;

    @Column(unique = true)
    @NotNull(message = "url null")
    String url;

    Boolean isTelegramGroupOrChannel;
}
