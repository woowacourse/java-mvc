package com.techcourse.repository;

import com.techcourse.domain.User;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static long INDEX = 1;
    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final User user = new User(1, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        injectAutoIncrementIndex(user);
        database.put(user.getAccount(), user);
    }

    private static void injectAutoIncrementIndex(User user) {
        try {
            Field id = user.getClass().getDeclaredField("id");
            id.setAccessible(true);
            id.setLong(user, ++INDEX);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {}
}
