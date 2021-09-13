package com.techcourse.service;

import com.techcourse.controller.request.RegisterRequest;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

public class RegisterService {

    private final InMemoryUserRepository userRepository;

    public RegisterService(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(RegisterRequest request) {
        User user = request.toEntity();
        userRepository.save(user);
    }
}
