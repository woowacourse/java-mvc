package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final var user = new User(1, "gugu", "password", "hkkang@woowahan.com");
        final var user2 = new User(2, "a", "123", "123@woowahan.com");
        database.put(user.getAccount(), user);
        database.put(user2.getAccount(), user2);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {}

    public static List<User> findAll() {
        return new ArrayList<>(database.values());
    }
}
