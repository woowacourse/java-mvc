package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.LoginFailedException;
import com.techcourse.exception.UserNotFoundException;
import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.service.dto.LoginDto;

public class LoginService {

    public User login(LoginDto loginDto) {
        User user = InMemoryUserRepository.findByAccount(loginDto.getAccount())
            .orElseThrow(UserNotFoundException::new);
        if (!user.checkPassword(loginDto.getPassword())) {
            throw new LoginFailedException();
        }
        return user;
    }
}
