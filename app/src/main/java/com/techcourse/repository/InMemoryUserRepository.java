package com.techcourse.repository;

import com.techcourse.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();
    private static Long id = 1L;

    static {
        final User user = new User(id, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    private InMemoryUserRepository() {
    }

    public static void save(User user) {
        User saveUser = new User(++id, user.getAccount(), user.getPassword(), user.getEmail());
        database.put(user.getAccount(), saveUser);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public static List<User> findAll() {
        return new ArrayList<>(database.values());
    }
}
