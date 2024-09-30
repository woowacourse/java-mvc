package di.stage2.constructorwithinterfaces;

import di.User;

public class UserDaoFactory {
    public static UserDao getProductionUserDao() {
        return new InMemoryUserDao();
    }

    public static UserDao getTestUserDao() {
        return new UserDao() {
            private User user;

            @Override
            public void insert(User user) {
                this.user = user;
            }

            @Override
            public User findById(long id) {
                return user;
            }
        };
    }
}
