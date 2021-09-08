package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> DATABASE;
    private static Long index = 1L;

    static {
        DATABASE = new ConcurrentHashMap<>();
        saveInitUserData();
    }

    private InMemoryUserRepository() {
    }

    private static void saveInitUserData() {
        final User user = new User("gugu", "password", "hkkang@woowahan.com");
        save(user);
    }

    public static User save(User user) {
        final User userWithId = new User(index++, user.getAccount(), user.getPassword(), user.getEmail());
        DATABASE.put(userWithId.getAccount(), userWithId);
        return userWithId;
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(DATABASE.get(account));
    }

    public static boolean existsByAccount(String account) {
        return DATABASE.containsKey(account);
    }

    public static boolean existsByEmail(String email) {
        return DATABASE.values().stream()
                .anyMatch(userInDB -> userInDB.hasSameEmail(email));
    }
}
