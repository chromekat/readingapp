package com.readly.booklist.users;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registeredUser(String username, String email, String rawPassword) {
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("Username already exists");
        }

        if(userRepository.existsByEmail(email)){
            throw new IllegalArgumentException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);

        User user = new User(username, email, hashedPassword);
        return userRepository.save(user);

    }

//    public Optional<User> getUserByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//
//    public boolean verifyPassword(String rawPassword, String hashedPassword) {
//        return passwordEncoder.matches(rawPassword, hashedPassword);
//    }
}
