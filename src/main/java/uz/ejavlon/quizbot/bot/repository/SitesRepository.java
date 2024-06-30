package uz.ejavlon.quizbot.bot.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ejavlon.quizbot.bot.entity.Site;

@Repository
public interface SitesRepository extends JpaRepository<Site,Integer> {

    boolean existsByUrl(@NotNull(message = "url or username null") String urlOrUsername);
}
