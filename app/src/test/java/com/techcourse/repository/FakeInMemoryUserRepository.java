package com.techcourse.repository;

import com.techcourse.domain.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FakeInMemoryUserRepository implements UserRepository {

    private final Map<String, User> database = new ConcurrentHashMap<>();

    @Override
    public void save(final User user) {
        database.put(user.getAccount(), user);
    }

    @Override
    public Optional<User> findByAccount(final String account) {
        return Optional.ofNullable(database.get(account));
    }
}
