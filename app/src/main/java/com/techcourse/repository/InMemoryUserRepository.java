package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    private static long id = 0;

    static {
        final User user = new User(++id, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    private InMemoryUserRepository() {
    }

    public static User save(User user) {
        user.setId(++id);
        database.put(user.getAccount(), user);
        return user;
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }
}
