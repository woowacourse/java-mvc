package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import nextstep.web.annotation.Repository;

@Repository
public class InMemoryUserRepository {

    private final Map<String, User> database = new ConcurrentHashMap<>();

    public InMemoryUserRepository() {
        final User gugu = new User(1, "gugu", "password", "hkkang@woowahan.com");
        final User philz = new User(2, "philz", "1234", "philz@hello.com");
        database.put(gugu.getAccount(), gugu);
        database.put(philz.getAccount(), philz);
    }

    public void save(User user) {
        database.put(user.getAccount(), user);
    }

    public Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }
}
