package di.stage0.staticreferences;

import di.User;

class UserService {

    private final UserDao userDao;

    UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User join(User user) {
        userDao.insert(user);
        return userDao.findById(user.getId());
    }
}
