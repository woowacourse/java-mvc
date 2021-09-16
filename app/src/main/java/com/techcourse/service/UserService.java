package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.NotFoundException;
import com.techcourse.repository.InMemoryUserRepository;

public class UserService {

    public User findByAccount(String account) {
        return InMemoryUserRepository.findByAccount(account)
                .orElseThrow(() -> new NotFoundException("account에 해당하는 User가 존재하지 않습니다."));
    }
}
