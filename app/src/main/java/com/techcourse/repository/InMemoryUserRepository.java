package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();
    private static Long index = 1L;

    static {
        final User user = new User(++index, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static User save(User user) {
        final User savedUser = new User(++index, user.getAccount(), user.getPassword(),
            user.getEmail());
        database.put(savedUser.getAccount(), savedUser);
        return savedUser;
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {}
}
