package nextstep.context.test_case_2;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import nextstep.web.annotation.Repository;

@Repository
public class InMemoryUserRepository implements UserDao {

    private final Map<String, User> database = new ConcurrentHashMap<>();

    public InMemoryUserRepository() {
        final User gugu = new User(1, "gugu", "password", "hkkang@woowahan.com");
        final User philz = new User(2, "philz", "1234", "philz@hello.com");
        database.put(gugu.getAccount(), gugu);
        database.put(philz.getAccount(), philz);
    }

    @Override
    public void save(User user) {
        database.put(user.getAccount(), user);
    }

    @Override
    public User findByAccount(String account) {
        return database.get(account);
    }
}
