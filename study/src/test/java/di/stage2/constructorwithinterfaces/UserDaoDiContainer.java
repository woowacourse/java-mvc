package di.stage2.constructorwithinterfaces;

import java.util.List;

public class UserDaoDiContainer {

    private static UserDaoDiContainer instance;
    private final List<UserDao> userDaos;

    public UserDaoDiContainer() {
        this.userDaos = List.of(
                new InMemoryUserDao()
        );
    }

    public static UserDaoDiContainer getInstance() {
        if (instance == null) {
            instance = new UserDaoDiContainer();
        }
        return instance;
    }

    public UserDao getUserDao(Class<?> daoClass) {
        for (UserDao userDao : userDaos) {
            if (daoClass.isInstance(userDao)) {
                return userDao;
            }
        }
        throw new IllegalArgumentException("No dao Found");
    }
}
