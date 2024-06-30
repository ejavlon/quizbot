package uz.ejavlon.quizbot.bot.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ejavlon.quizbot.base.dto.ResponseApi;
import uz.ejavlon.quizbot.bot.entity.Site;
import uz.ejavlon.quizbot.bot.service.SiteService;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/sites")
@RequiredArgsConstructor
@Tag(name = "Site Controller")
public class SiteController {

    private final SiteService siteService;


    @GetMapping
    public ResponseEntity<?> getAllSites(@RequestParam(defaultValue = "0") int page){
        return ResponseEntity.ok(siteService.getAllSites(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSiteById(@PathVariable Integer id){
        ResponseApi responseApi = siteService.getSiteByIdApi(id);
        return ResponseEntity.status(responseApi.getSuccess() ? OK : NOT_FOUND).body(responseApi);
    }

    @PostMapping
    public ResponseEntity<?> addSite(@Valid @RequestBody Site site){
        ResponseApi responseApi = siteService.addSite(site);
        return ResponseEntity.status(responseApi.getSuccess() ? CREATED : CONFLICT).body(responseApi);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> updateSite(@PathVariable Integer id, @RequestBody Site site){
        ResponseApi responseApi = siteService.updateSite(id,site);
        return ResponseEntity.status(responseApi.getSuccess() ? OK : NOT_FOUND).body(responseApi);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteSite(@PathVariable Integer id){
        ResponseApi responseApi = siteService.deleteSite(id);
        return ResponseEntity.status(responseApi.getSuccess() ? NO_CONTENT : NOT_FOUND).body(responseApi);
    }
}
