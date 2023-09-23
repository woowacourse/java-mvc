package di.stage1.constructorinjection;

import di.User;

import java.util.HashMap;
import java.util.Map;

class HashMapUserRepository implements UserRepository {

    private static final Map<Long, User> users = new HashMap<>();

    public void insert(User user) {
        users.put(user.getId(), user);
    }

    public User findById(long id) {
        return users.get(id);
    }
}
