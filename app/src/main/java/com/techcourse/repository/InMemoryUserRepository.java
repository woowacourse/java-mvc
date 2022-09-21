package com.techcourse.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.techcourse.domain.User;

public class InMemoryUserRepository {

    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final var user = new User(generateId(), "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        final User userToSave = new User(generateId(),
            user.getAccount(), user.getPassword(), user.getEmail());
        database.put(user.getAccount(), userToSave);
    }

    private InMemoryUserRepository() {
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    private static Long generateId() {
        return ID_GENERATOR.incrementAndGet();
    }
}
