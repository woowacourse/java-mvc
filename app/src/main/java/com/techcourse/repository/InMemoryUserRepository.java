package com.techcourse.repository;

import com.techcourse.domain.User;
import com.techcourse.exception.OutOfUserIdException;
import com.techcourse.exception.ReflectionException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    private static AtomicLong id = new AtomicLong(1);

    static {
        final User user = new User(1L, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        User userWithId = toUserWithId(user);
        database.put(userWithId.getAccount(), userWithId);
    }

    private static User toUserWithId(User user) {
        if (isMaximum()) {
            throw new OutOfUserIdException();
        }
        try {
            Class userClass = user.getClass();
            Field userId = userClass.getDeclaredField("id");
            if (userId.trySetAccessible()) {
                userId.set(user, id.incrementAndGet());
            }
        } catch (Exception e) {
            throw new ReflectionException();
        }

        return user;
    }

    private static boolean isMaximum() {
        return id.get() == Long.MAX_VALUE;
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }
}
