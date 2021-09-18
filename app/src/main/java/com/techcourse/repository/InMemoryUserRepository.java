package com.techcourse.repository;

import com.techcourse.AppWebApplicationInitializer;
import com.techcourse.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryUserRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private static final AtomicLong id = new AtomicLong(1L);
    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final User user = new User("gugu", "password", "hkkang@woowahan.com");
        save(user);
    }

    private InMemoryUserRepository() {
    }

    public static void save(User user) {
        user.setId(id.getAndIncrement());
        log.info("save user! - id : {}, account: {}", user.getId(), user.getAccount());
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public static List<User> findAll() {
        return new ArrayList<>(database.values());
    }
}
