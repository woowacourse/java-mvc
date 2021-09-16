package com.techcourse.repository;

import com.techcourse.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private static final Map<String, User> database = new ConcurrentHashMap<>();
    private static Long autoIncrementId = 1L;

    static {
        final User user = new User(autoIncrementId, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        log.debug("Saved User : {}", user.toString());
        autoIncrementId++;
        database.put(user.getAccount(), new User(autoIncrementId, user.getAccount(), user.getPassword(), user.getEmail()));
    }

    public static Optional<User> findByAccount(String account) {
        log.debug("findByAccount User : {}", account);
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {}
}
