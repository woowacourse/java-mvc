package com.techcourse.repository;

import com.techcourse.domain.User;
import com.techcourse.exception.DuplicatedUserException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final User user = new User(1, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        Optional<User> byAccount = findByAccount(user.getAccount());
        if (byAccount.isPresent()) {
            throw new DuplicatedUserException("이미 저장된 계정입니다.");
        }
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public static void removeAll() {
        database.clear();
    }

    private InMemoryUserRepository() {}
}
