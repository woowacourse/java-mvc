package di.stage0.staticreferences;

import di.User;

class UserService {

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    private UserDao userDao;

    public User join(User user) {
        userDao.insert(user);
        return userDao.findById(user.getId());
    }
}
