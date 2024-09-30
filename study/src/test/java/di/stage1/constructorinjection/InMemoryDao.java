package di.stage1.constructorinjection;

import di.User;

public interface InMemoryDao {

    void insert(User user);

    User findById(long id);
}
