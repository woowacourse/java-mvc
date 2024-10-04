package di.stage1.constructorinjection;

import di.User;

class UserService {

    private final InMemoryDao userDao;

    public UserService(InMemoryDao userDao) {
        this.userDao = userDao;
    }

    public User join(User user) {
        userDao.insert(user);
        return userDao.findById(user.getId());
    }
}
