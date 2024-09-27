package com.techcourse.service;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService = new UserService();

    public void login(String account, String password, HttpSession session) throws AuthException {
        if (isAlreadyLogin(session)) {
            return;
        }

        User user = findUserByAccount(account);
        if (!user.checkPassword(password)) {
            throw new AuthException("로그인에 실패하였습니다");
        }
        session.setAttribute(UserSession.SESSION_KEY, user);
        log.info("User : {}", user);
    }

    public void loginWithSession(HttpSession session) throws AuthException {
        User user1 = UserSession.getUserFrom(session)
                .orElseThrow(() -> new AuthException("유저 세션을 찾을 수 없습니다."));
        log.info("logged in {}", user1.getAccount());
    }

    private boolean isAlreadyLogin(HttpSession session) {
        return UserSession.isLoggedIn(session);
    }

    private User findUserByAccount(String account) throws AuthException {
        return userService.findByAccount(account)
                .orElseThrow(() -> new AuthException(account + "계정을 가진 유저가 존재하지 않습니다"));
    }

    public void logout(HttpSession session) {
        session.removeAttribute(UserSession.SESSION_KEY);
    }
}
