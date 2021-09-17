package com.techcourse.repository;

import com.techcourse.domain.User;
import com.techcourse.repository.exception.SaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
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

    public static void save(User user)  {
        try {
            setAutoIncrementId(user);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new SaveException();
        }

        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        log.debug("findByAccount User : {}", account);
        return Optional.ofNullable(database.get(account));
    }


    private static void setAutoIncrementId(User user) throws NoSuchFieldException, IllegalAccessException {
        Field id = user.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(user, ++autoIncrementId);

    }

    private InMemoryUserRepository() {
    }
}
