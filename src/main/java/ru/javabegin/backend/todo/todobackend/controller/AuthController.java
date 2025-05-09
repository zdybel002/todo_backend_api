package ru.javabegin.backend.todo.todobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javabegin.backend.todo.todobackend.entity.User;
import ru.javabegin.backend.todo.todobackend.repo.UserRepository;
import ru.javabegin.backend.todo.todobackend.search.LoginRequest;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getPassword().equals(request.getPassword())) {
                return ResponseEntity.ok(String.valueOf(user.getId()));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Błąd logowania");
    }

}
