package di.stage3.context;

import java.util.Set;

public class BeanInitializer {

    public Set<Object> initialize(final Set<Class<?>> classes) throws Exception {
        final Class<?> aClass = classes.stream()
                .filter(clazz -> clazz == UserService.class)
                .findAny()
                .orElseThrow();
        final UserDao dao = new InMemoryUserDao();
        final UserService userService = (UserService) aClass.getDeclaredConstructor(UserDao.class)
                .newInstance(dao);
        return Set.of(userService, dao);
    }
}
