package com.techcourse.repository;

import com.techcourse.domain.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();
    private static final User NOT_EXIST_USER = null;

    static {
        var user = new User(1, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        if (account == null) {
            return Optional.ofNullable(NOT_EXIST_USER);
        }
        return Optional.ofNullable(database.getOrDefault(account, NOT_EXIST_USER));
    }

    private InMemoryUserRepository() {
    }
}
