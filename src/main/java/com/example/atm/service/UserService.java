package com.example.atm.service;

import com.example.atm.model.User;
import com.example.atm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUserBalance(int userId, int newBalance) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setBalance(newBalance);
            userRepository.save(user);
        }
        return user;
    }
}
