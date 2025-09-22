package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final var user = new User(1, "gugu", "password", "hkkang@woowahan.com");
        final var user2 = new User(2, "gakong", "password2", "hkkang@woowahan.com2");
        final var user3 = new User(2, "free", "password2", "hkkang@woowahan.com2");
        database.put(user.getAccount(), user);
        database.put(user2.getAccount(), user2);
        database.put(user3.getAccount(), user3);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {}
}
