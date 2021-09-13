package com.techcourse.service;

import com.techcourse.controller.UserSession;
import com.techcourse.controller.request.LoginRequest;
import com.techcourse.domain.User;
import com.techcourse.exception.UnauthorizedException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpSession;

public class LoginService {

    private final InMemoryUserRepository userRepository;

    public LoginService(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(LoginRequest request) {
        User user = findUserByAccount(request.getAccount());
        user.checkPassword(request.getPassword());

        HttpSession session = request.getHttpSession();
        session.setAttribute(UserSession.SESSION_KEY, user);
    }

    private User findUserByAccount(String account) {
        return userRepository.findByAccount(account)
            .orElseThrow(UnauthorizedException::new);
    }
}
