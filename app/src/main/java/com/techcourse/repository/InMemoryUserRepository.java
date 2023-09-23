package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final var gugu = new User(1, "gugu", "password", "hkkang@woowahan.com");
        final var hyena = new User(2, "hyena", "password", "hyena@woowahan.com");
        final var herb = new User(3, "herb", "password", "herb@woowahan.com");
        database.put(gugu.getAccount(), gugu);
        database.put(hyena.getAccount(), hyena);
        database.put(herb.getAccount(), herb);
    }

    private InMemoryUserRepository() {
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }
}
