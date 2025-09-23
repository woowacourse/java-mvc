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
        final var user1 = new User(1, "gugu", "password1", "hkkang@woowahan.com");
        final var user2 = new User(2, "sana", "password2", "sana@woowahan.com");
        final var user3 = new User(3, "yulmoo", "password3", "yulmoo@woowahan.com");
        database.put(user1.getAccount(), user1);
        database.put(user2.getAccount(), user2);
        database.put(user3.getAccount(), user3);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public static List<User> findAll() {
        return new ArrayList<>(database.values());
    }

    private InMemoryUserRepository() {
    }
}
