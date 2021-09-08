package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.UnAuthorizedException;
import com.techcourse.repository.InMemoryUserRepository;

public class LoginService {

    private static final String LOGIN_FAILURE_EXCEPTION_MESSAGE = "로그인 정보가 잘못되었습니다.";

    public User login(String account, String password) {
        final User foundUser = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(() -> new UnAuthorizedException(LOGIN_FAILURE_EXCEPTION_MESSAGE));
        if (!foundUser.checkPassword(password)) {
            throw new UnAuthorizedException(LOGIN_FAILURE_EXCEPTION_MESSAGE);
        }
        return foundUser;
    }
}
