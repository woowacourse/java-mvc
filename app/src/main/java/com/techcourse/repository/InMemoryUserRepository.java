package com.techcourse.repository;

import com.techcourse.domain.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository {

    private static final AtomicLong RESERVED_ID = new AtomicLong(0);

    public static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        save(new User("gugu", "password", "hkkang@woowahan.com"));
    }

    public static void save(final User user) {
        user.setId(RESERVED_ID.incrementAndGet());
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(final String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {
    }
}
