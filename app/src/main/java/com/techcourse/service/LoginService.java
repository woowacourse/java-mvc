package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.LoginFailedException;
import com.techcourse.exception.UserNotFoundException;
import com.techcourse.repository.InMemoryUserRepository;

public class LoginService {
    public User login(String account, String password) {
        User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(UserNotFoundException::new);
        if (!user.checkPassword(password)) {
            throw new LoginFailedException();
        }
        return user;
    }
}
