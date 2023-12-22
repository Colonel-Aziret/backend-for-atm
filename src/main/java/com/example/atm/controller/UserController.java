package com.example.atm.controller;

import com.example.atm.model.User;
import com.example.atm.repository.UserRepository;
import com.example.atm.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/exit")
    public String exit() {
        return "Have a good day, bye";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userService.getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            // Авторизация успешна, возвращаем баланс в формате JSON
            Map<String, Object> response = new HashMap<>();
            response.put("balance", user.getBalance());

            return ResponseEntity.ok(response);
        } else {
            // Неверные учетные данные, возвращаем ошибку
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Неверные данные для входа");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        }
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Object>getUserBalance(@PathVariable int id) {
        User user = userService.getUserById(id);

        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("balance", user.getBalance());
            return ResponseEntity.ok(response);
        } else {
            System.out.println("Пользователь с таким id не найден");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Неверные данные для входа");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/{id}/deposit")
    public User deposit(@PathVariable int id, @RequestBody Map<String, Integer> requestBody) {
        int amount = requestBody.get("balance");
        User user = userService.getUserById(id);

        if (user != null) {
            int newBalance = user.getBalance() + amount;
            return userService.updateUserBalance(id, newBalance);
        } else {
            // Обработка ситуации, когда пользователь не найден
            return null; // или выбросить исключение, в зависимости от требований
        }
    }


    @PostMapping("/{id}/withdraw")
    public User withdraw(@PathVariable int id, @RequestBody Map<String, Integer> requestBody) {
        int amount = requestBody.get("balance");
        User user = userService.getUserById(id);

        if (user != null) {
            if (user.getBalance() >= amount) {
                int newBalance = user.getBalance() - amount;
                return userService.updateUserBalance(id, newBalance);
            } else {
                // Обработка ситуации, когда недостаточно средств на счете
                System.out.println("Недостаточно средств на счете");
                return null; // или выбросить исключение, в зависимости от требований
            }
        } else {
            // Обработка ситуации, когда пользователь не найден
            System.out.println("Пользователь не найден");
            return null; // или выбросить исключение, в зависимости от требований
        }
    }
}
