package uz.ejavlon.quizbot.bot.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ejavlon.quizbot.base.dto.ResponseApi;
import uz.ejavlon.quizbot.bot.service.TelegramUserService;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/telegramUsers")
@RequiredArgsConstructor
@Tag(name = "TelegramUser Controller")
public class TelegramUserController {

    private final TelegramUserService userService;

    @GetMapping
    public HttpEntity<?> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(userService.getAllUsers(pageNumber));
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteUser(@PathVariable Long id){
        ResponseApi responseApi = userService.deleteUser(id);
        return ResponseEntity.status(responseApi.getSuccess() ? NO_CONTENT : NOT_FOUND).body(responseApi);
    }

    @PutMapping("/block/{id}")
    public HttpEntity<?> setBlockUser(@PathVariable Long id){
        ResponseApi responseApi = userService.setBlockUser(id);
        return ResponseEntity.status(responseApi.getSuccess() ? OK : NOT_FOUND).body(responseApi);
    }

    @PutMapping("/activated/{id}")
    public HttpEntity<?> setActiveUser(@PathVariable Long id){
        ResponseApi responseApi = userService.setActiveUser(id);
        return ResponseEntity.status(responseApi.getSuccess() ? OK : NOT_FOUND).body(responseApi);
    }
}
