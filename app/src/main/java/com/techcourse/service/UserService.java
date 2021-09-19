package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.UserNotFoundException;
import com.techcourse.repository.InMemoryUserRepository;
import nextstep.web.annotation.Service;

@Service
public class UserService {

    private final InMemoryUserRepository userRepository;

    public UserService(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByAccount(String account) {
        return userRepository.findByAccount(account)
            .orElseThrow(UserNotFoundException::new);
    }
}
