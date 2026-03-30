package com.settleup.backend.service;

import org.springframework.stereotype.Service;

import com.settleup.backend.entity.User;
import com.settleup.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOrCreateUser(String name, String email) {

        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    try {
                        User newUser = new User();
                        newUser.setName(name);
                        newUser.setEmail(email);
                        newUser.setPassword("default123");

                        return userRepository.save(newUser);

                    } catch (Exception e) {
                        // handle race condition
                        return userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User creation failed"));
                    }
                });
    }
}