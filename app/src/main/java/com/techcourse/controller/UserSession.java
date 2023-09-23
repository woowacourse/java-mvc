package com.techcourse.controller;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UserSession {

    private static final Logger log = LoggerFactory.getLogger(UserSession.class);

    public static final String SESSION_KEY = "user";

    private UserSession() {}

    public static boolean isLoggedIn(final HttpSession session) {
        return getUserFrom(session).isPresent();
    }

    public static Optional<User> getUserFrom(final HttpSession session) {
        final User user = (User) session.getAttribute(SESSION_KEY);
        log.info("{} ==========> Maybe User = {}", UserSession.class.getSimpleName(), user);
        return Optional.ofNullable(user);
    }
}
