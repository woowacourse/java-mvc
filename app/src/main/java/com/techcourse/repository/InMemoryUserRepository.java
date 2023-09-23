package com.techcourse.repository;

import com.techcourse.domain.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository {

    private static final AtomicLong userId = new AtomicLong(0);
    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final User registerUser = new User("gugu", "password", "hkkang@woowahan.com");
        final User persistUser = new User(userId.incrementAndGet(), registerUser);

        database.put(registerUser.getAccount(), persistUser);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {
    }
}
