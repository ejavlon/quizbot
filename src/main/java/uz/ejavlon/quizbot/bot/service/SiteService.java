package uz.ejavlon.quizbot.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.ejavlon.quizbot.base.dto.ResponseApi;
import uz.ejavlon.quizbot.bot.entity.Site;
import uz.ejavlon.quizbot.bot.repository.SitesRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteService {

    private final SitesRepository sitesRepository;

    public ResponseApi getAllSites(int page){
        Pageable pageable = PageRequest.of(page, 5);
        return ResponseApi.builder()
                .success(true)
                .message("All Sites")
                .data(sitesRepository.findAll(pageable))
                .build();
    }

    public List<Site> getAllSites(){
        return sitesRepository.findAll();
    }

    public ResponseApi getSiteByIdApi(Integer id){
        Site result = sitesRepository.findById(id).orElse(null);
        return ResponseApi.builder()
                .data(result)
                .success(Objects.nonNull(result))
                .message(Objects.nonNull(result) ? "Site found!" : "Site not found!")
                .build();
    }

    public Site getSiteById(Integer id){
        return sitesRepository.findById(id).orElse(null);
    }

    public ResponseApi addSite(Site site){
        if (sitesRepository.existsByUrl(site.getUrl() ))
            return ResponseApi.builder()
                    .message("Site exists")
                    .data(site)
                    .success(false)
                    .build();

        site = sitesRepository.save(site);

        return ResponseApi.builder()
                .data(site)
                .message("Site successfully saved")
                .success(true)
                .build();
    }

    public ResponseApi updateSite(Integer id, Site site) {
        Optional<Site> optionalSite = sitesRepository.findById(id);
        if (optionalSite.isEmpty())
            return ResponseApi.builder()
                    .message("Site not found")
                    .success(false)
                    .build();

        Site editedSite = optionalSite.get();
        editedSite.setUrl(site.getUrl());
        editedSite.setName(site.getName());
        editedSite.setIsTelegramGroupOrChannel(site.getIsTelegramGroupOrChannel());
        editedSite = sitesRepository.save(editedSite);

        return ResponseApi.builder()
                .message("Site successfully updated")
                .success(true)
                .data(editedSite)
                .build();
    }

    public ResponseApi deleteSite(Integer id){
        Optional<Site> optionalSite = sitesRepository.findById(id);
        if (optionalSite.isEmpty())
            return ResponseApi.builder()
                    .message("Site not found")
                    .success(false)
                    .build();

        sitesRepository.delete(optionalSite.get());
        return ResponseApi.builder()
                .data(optionalSite.get())
                .message("Site successfully deleted!")
                .success(true)
                .build();
    }
}
