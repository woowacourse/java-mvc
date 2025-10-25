package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final var user = new User(1, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public static Collection<User> findAll() {
        return database.values();
    }

    public static int getNextId() {
        return database.values().stream()
                .mapToInt(user -> (int) user.getId())
                .max()
                .orElse(0) + 1;
    }

    private InMemoryUserRepository() {}
}
