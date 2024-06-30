package uz.ejavlon.quizbot.bot.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ejavlon.quizbot.base.dto.ResponseApi;
import uz.ejavlon.quizbot.bot.entity.Question;
import uz.ejavlon.quizbot.bot.service.QuestionService;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
@Tag(name = "Question Controller")
@CrossOrigin
public class QuestionController {

    private final QuestionService questionService;


    @GetMapping
    public HttpEntity<?> getAllQuestion(@RequestParam(defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(questionService.getAllQuestions(pageNumber));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getQuestionById(@PathVariable UUID id){
        ResponseApi question = questionService.getQuestionById(id);
        return ResponseEntity.status(question.getSuccess() ? OK : NOT_FOUND).body(question);
    }

    @PostMapping
    public HttpEntity<?> addQuestion(@RequestBody Question question){
        ResponseApi newQuestion = questionService.addQuestion(question);
        return ResponseEntity.status(newQuestion.getSuccess() ? CREATED : CONFLICT).body(newQuestion);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> updateQuestion(@PathVariable UUID id, @RequestBody Question question){
        ResponseApi updatedQuestion = questionService.updateQuestion(id, question);
        return ResponseEntity.status(updatedQuestion.getSuccess() ? OK : NOT_FOUND).body(question);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteQuestion(@PathVariable UUID id){
        ResponseApi responseApi = questionService.deleteQuestion(id);
        return ResponseEntity.status(responseApi.getSuccess() ? OK : NOT_FOUND).body(responseApi);
    }
}
