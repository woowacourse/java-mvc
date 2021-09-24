package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final User user1 = new User(1, "gugu", "password", "hkkang@woowahan.com");
        final User user2 = new User(2, "sakjung", "password", "sakjung@woowahan.com");
        database.put(user1.getAccount(), user1);
        database.put(user2.getAccount(), user2);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public static List<User> findAll() {
        return database.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    private InMemoryUserRepository() {}
}
