package di.stage3.context;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans = new HashSet<>();

    public DIContainer(final Set<Class<?>> classes) {
        initBeans(classes);
    }

    private void initBeans(Set<Class<?>> classes) {
        Class<?> aClass = classes.stream()
                .filter(clazz -> clazz == UserService.class)
                .findAny()
                .orElseThrow();
        UserDao dao = new InMemoryUserDao();
        UserService userService;
        try {
            userService = (UserService) aClass.getDeclaredConstructor(UserDao.class)
                    .newInstance(dao);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        beans.add(userService);
        beans.add(dao);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .map(aClass::cast)
                .findFirst()
                .orElse(null);
    }
}
