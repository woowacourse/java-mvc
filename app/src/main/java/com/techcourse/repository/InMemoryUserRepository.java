package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final int NEXT_USER = 1;

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final var user = new User(NEXT_USER, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public static int getNextUserId() {
        return database.size() + NEXT_USER;
    }

    private InMemoryUserRepository() {}
}
