package di.stage0.staticreferences;

import di.User;
import java.util.HashMap;
import java.util.Map;

class UserDao {

    private final Map<Long, User> users = new HashMap<>();

    public void insert(User user) {
        users.put(user.getId(), user);
    }

    public User findById(long id) {
        return users.get(id);
    }
}
