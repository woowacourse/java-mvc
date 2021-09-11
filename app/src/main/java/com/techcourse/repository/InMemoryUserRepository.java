package com.techcourse.repository;

import com.techcourse.domain.User;
import com.techcourse.exception.UserException;
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
        String account = user.getAccount();
        if (database.containsKey(account)) {
            throw new UserException("중복된 이름입니다.");
        }
        database.put(account, user);
    }

    public static void save(String account, String password, String email) {
        save(new User(count() + 1L, account, password, email));
    }

    public static long count() {
        return database.size();
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {
    }
}
