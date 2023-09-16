package com.techcourse.repository;

import com.techcourse.domain.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();
    private static long index = 1;

    static {
        final var user = new User("gugu", "password", "hkkang@woowahan.com");
        save(user);
    }

    public static void save(User user) {
        if (user.getId() == null) {
            user.setId(index);
            index += 1;
        }
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {
    }
}
