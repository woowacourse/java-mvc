package com.techcourse.repository;

import com.techcourse.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final User user1 = new User(1, "gugu", "password", "hkkang@woowahan.com");
        final User user2 = new User(2, "pobi", "password", "hkkang@woowahan.com");
        final User user3 = new User(3, "cu", "password", "hkkang@woowahan.com");
        final User user4 = new User(4, "woni", "password", "hkkang@woowahan.com");

        database.put(user1.getAccount(), user1);
        database.put(user2.getAccount(), user2);
        database.put(user3.getAccount(), user3);
        database.put(user4.getAccount(), user4);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public static List<User> findAll(){
        List<User> users = new ArrayList<>();

        database.entrySet().stream()
            .forEach(it -> users.add(it.getValue()));
        return users;
    }

    private InMemoryUserRepository() {}
}
