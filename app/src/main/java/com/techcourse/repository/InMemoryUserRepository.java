package com.techcourse.repository;

import com.techcourse.domain.Account;
import com.techcourse.domain.Email;
import com.techcourse.domain.Password;
import com.techcourse.domain.User;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository {

    private static final AtomicLong atomicLong = new AtomicLong(1);
    private static final Map<Long, User> database = new ConcurrentHashMap<>();

    static {
        Account account = new Account("gugu");
        Password password = new Password("password");
        Email email = new Email("hkkang@woowahan.com");
        final var user = new User(account, password, email);
        database.put(atomicLong.getAndIncrement(), user);
    }

    public static void save(final User user) {
        database.put(atomicLong.getAndIncrement(), user);
    }

    public static Optional<User> findByAccount(final Account account) {
        for (User user : database.values()) {
            if (user.checkAccount(account)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private InMemoryUserRepository() {}
}
