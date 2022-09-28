package com.techcourse.repository;

import com.techcourse.domain.User;

import com.techcourse.exception.UserRuntimeException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository {

    private static final Map<Long, User> DATABASE = new ConcurrentHashMap<>();
    private static final Map<String, Long> INDEX_TABLE = new ConcurrentHashMap<>();
    private static final AtomicLong ID_COUNTER = new AtomicLong(0);

    private InMemoryUserRepository() {}

    static {
        final var user = new User("gugu", "password", "hkkang@woowahan.com");
        save(user);
    }

    public static void save(User user) {
        if (!Objects.isNull(INDEX_TABLE.get(user.getAccount()))) {
            throw new UserRuntimeException("이미 존재하는 계정이름 입니다.");
        }
        final User newUser = user.newInstanceWithId(ID_COUNTER.incrementAndGet());
        DATABASE.put(newUser.getId(), newUser);
        INDEX_TABLE.put(newUser.getAccount(), newUser.getId());
    }

    public static Optional<User> findByAccount(String account) {
        final Long userId = INDEX_TABLE.get(account);
        if (Objects.isNull(userId)) {
            return Optional.empty();
        }
        return Optional.ofNullable(DATABASE.get(userId));
    }
}
