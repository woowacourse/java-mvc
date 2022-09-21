package com.techcourse.repository;

import com.techcourse.domain.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static long ID_SEQUENCE = 1L;
    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final var user = new User(ID_SEQUENCE, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        final var userIdAssigned = assignIdToUser(user);

        database.put(userIdAssigned.getAccount(), userIdAssigned);
    }

    private synchronized static User assignIdToUser(User user) {
        return user.assignId(++ID_SEQUENCE);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {
    }
}
