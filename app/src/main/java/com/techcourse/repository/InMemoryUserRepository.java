package com.techcourse.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.techcourse.domain.User;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    public static final AtomicLong idCounter = new AtomicLong();

    static {
        final var user = new User(idCounter.incrementAndGet(), "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(final User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(final String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {
    }
}
