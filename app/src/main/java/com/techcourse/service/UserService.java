package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserService {

    public User register(User user) {
        InMemoryUserRepository.save(user);
        return InMemoryUserRepository.findByAccount(user.getAccount())
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
    }

    public Optional<User> findByAccount(String account) {
        return InMemoryUserRepository.findByAccount(account);
    }
}
