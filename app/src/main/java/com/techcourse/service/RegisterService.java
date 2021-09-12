package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

public class RegisterService {

    public User join(String account, String password, String email) {
        final User user = new User(account, password, email);
        return InMemoryUserRepository.save(user);
    }
}
