package uz.ejavlon.quizbot.base.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.ejavlon.quizbot.base.dto.ResponseApi;
import uz.ejavlon.quizbot.base.dto.SignInDto;
import uz.ejavlon.quizbot.base.dto.SignUpDto;
import uz.ejavlon.quizbot.base.entity.User;
import uz.ejavlon.quizbot.base.enums.Role;
import uz.ejavlon.quizbot.base.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public ResponseApi signIn(SignInDto signInDto){
        Optional<User> optionalUser = userRepository.findByUsername(signInDto.getUsername());
        if (optionalUser.isEmpty())
            return ResponseApi.builder()
                    .success(false)
                    .message("No such user")
                    .build();

        User user = optionalUser.get();
        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword()))
            return ResponseApi.builder()
                    .success(false)
                    .message("No such user")
                    .build();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInDto.getUsername(),
                        signInDto.getPassword()
                )
        );

        return ResponseApi.builder()
                .success(true)
                .message("Welcome to system")
                .data(jwtService.generateToken(user))
                .build();
    }

    public ResponseApi signUp(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        if (optionalUser.isPresent())
            return ResponseApi.builder()
                    .success(true)
                    .message("such username is registered in the system")
                    .build();

        var user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.USER)
                .build();
        user = userRepository.save(user);

        return ResponseApi.builder()
                .success(true)
                .message("Successfully created")
                .data(user)
                .build();
    }

    public ResponseApi getAllUsers() {
        return ResponseApi.builder()
                .success(true)
                .message("All users")
                .data(userRepository.findAll())
                .build();
    }

    public ResponseApi editUserById(Integer id,SignUpDto user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            return ResponseApi.builder()
                    .success(false)
                    .message("User not found")
                    .build();

        User editedUser = optionalUser.get();

        Optional<User> optionalUserByUsername = userRepository.findByUsername(user.getUsername());
        if (optionalUserByUsername.isPresent()){
            User user1 = optionalUserByUsername.get();
            if (!Objects.equals(user1.getId(), editedUser.getId()))
                return ResponseApi.builder()
                        .success(true)
                        .message("such username is registered in the system")
                        .build();
        }

        editedUser.setFirstName(user.getFirstName());
        editedUser.setLastName(user.getLastName());
        editedUser.setPassword(user.getPassword());
        editedUser.setUsername(user.getUsername());
        editedUser = userRepository.save(editedUser);

        return ResponseApi.builder()
                .success(true)
                .message("Successfully updated")
                .data(editedUser)
                .build();
    }

    public ResponseApi deleteUserById(Integer id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            return ResponseApi.builder()
                    .success(false)
                    .message("User not found")
                    .build();

        userRepository.deleteById(id);

        return ResponseApi.builder()
                .success(true)
                .message("Successfully deleted")
                .build();
    }
}
