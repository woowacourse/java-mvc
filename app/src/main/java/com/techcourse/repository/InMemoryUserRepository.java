package com.techcourse.repository;

import com.techcourse.domain.Account;
import com.techcourse.domain.User;
import com.techcourse.exception.UserRuntimeException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryUserRepository {

    private static final Map<Long, User> DATABASE = new ConcurrentHashMap<>();
    private static final Map<Account, Long> INDEX_TABLE = new ConcurrentHashMap<>();
    private static final AtomicLong ID_COUNTER = new AtomicLong(0);
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private InMemoryUserRepository() {
    }

    static {
        final User user = new User(
            "gugu",
            "password",
            "hkkang@woowahan.com");
        save(user);
    }

    public static void save(User user) {
        if (!Objects.isNull(INDEX_TABLE.get(user.account()))) {
            throw new UserRuntimeException("존재하는 계정명 입니다.");
        }

        final User identifiedUser = user.newInstanceWithId(ID_COUNTER.incrementAndGet());
        log.info(identifiedUser.toString());
        DATABASE.put(identifiedUser.id(), identifiedUser);
        INDEX_TABLE.put(identifiedUser.account(), identifiedUser.id());
    }

    public static Optional<User> findByAccount(String account) {
        Long userId = INDEX_TABLE.get(new Account(account));
        if (Objects.isNull(userId)) {
            return Optional.empty();
        }
        return Optional.ofNullable(DATABASE.get(userId));
    }
}
