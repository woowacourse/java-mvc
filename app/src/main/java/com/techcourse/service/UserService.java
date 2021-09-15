package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.UserNotFoundException;
import com.techcourse.repository.InMemoryUserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    public User findByAccount(String account) {
        return InMemoryUserRepository.findByAccount(account)
                .orElseThrow(UserNotFoundException::new);
    }

    public List<User> findByAccounts(String[] accounts) {
        List<User> users = new ArrayList<>();
        for (String account : accounts) {
            final User user = InMemoryUserRepository.findByAccount(account)
                    .orElseThrow(UserNotFoundException::new);
            users.add(user);
        }
        return users;
    }
}
