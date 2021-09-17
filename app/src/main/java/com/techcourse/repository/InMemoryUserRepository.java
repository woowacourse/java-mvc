package com.techcourse.repository;

import com.techcourse.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import nextstep.core.annotation.Component;

@Component
public class InMemoryUserRepository {

    private final Map<String, User> database = new ConcurrentHashMap<>();
    private Long id = 0L;

    public InMemoryUserRepository() {
        final User user = new User(++id, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public void save(User user) {
        user.setId(++id);
        database.put(user.getAccount(), user);
    }

    public Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public List<User> findAll() {
        return new ArrayList<>(database.values());
    }
}
