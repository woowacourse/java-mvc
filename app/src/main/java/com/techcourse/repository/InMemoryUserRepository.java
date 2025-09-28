package com.techcourse.repository;

import com.techcourse.domain.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryUserRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final var gugu = new User(1, "gugu", "password", "hkkang@woowahan.com");
        database.put(gugu.getAccount(), gugu);

        log.info("InMemoryUserRepository initialized. Users: {}", database.keySet());
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        log.info("Searching for account: [{}]. Current users in DB: {}", account, database.keySet());
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {}
}