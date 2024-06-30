package uz.ejavlon.quizbot.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.ejavlon.quizbot.base.dto.ResponseApi;
import uz.ejavlon.quizbot.base.dto.SignInDto;
import uz.ejavlon.quizbot.base.dto.SignUpDto;
import uz.ejavlon.quizbot.base.service.UserService;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDto signInDto){
        ResponseApi responseApi = userService.signIn(signInDto);
        return ResponseEntity.status(responseApi.getSuccess() ? OK : FORBIDDEN).body(responseApi);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto userDto){
        ResponseApi responseApi = userService.signUp(userDto);
        return ResponseEntity.status(responseApi.getSuccess() ? CREATED : CONFLICT).body(responseApi);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(
            description = "Get endpoint for admin",
            summary = "Login as admin to access this endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    )
            }
    )
    public ResponseEntity<?> getAllUsers(){
        ResponseApi responseApi = userService.getAllUsers();
        return ResponseEntity.ok(responseApi);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(
            description = "Put endpoint for admin",
            summary = "Login as admin to access this endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "There is no such endpoint",
                            responseCode = "404"

                    )
            }
    )
    public ResponseEntity<?> editUserById(@PathVariable Integer id, @Valid SignUpDto user){
        ResponseApi responseApi = userService.editUserById(id,user);
        return ResponseEntity.status(responseApi.getSuccess() ? OK : NOT_FOUND).body(responseApi);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/users/{id}")
    @Operation(
            description = "Delete endpoint for admin",
            summary = "Login as admin to access this endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "There is no such endpoint",
                            responseCode = "404"
                    )
            }
    )
    public ResponseEntity<?> deleteUserById(@PathVariable Integer id){
        ResponseApi responseApi = userService.deleteUserById(id);
        return ResponseEntity.status(responseApi.getSuccess() ? NO_CONTENT: NOT_FOUND).body(responseApi);
    }

}
