package di.stage3.context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();

        final Class<?> userDao = classes.stream()
            .filter(clazz -> Arrays.asList(clazz.getInterfaces()).contains(UserDao.class))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("UserDao가 존재하지 않습니다."));

        final Class<?> userService = classes.stream()
            .filter(clazz -> clazz.isAssignableFrom(UserService.class))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("UserService가 존재하지 않습니다."));

        try {
            final UserDao userDaoInstance = (UserDao) userDao.getConstructor().newInstance();
            beans.add(userDaoInstance);
            beans.add(userService.getConstructor(UserDao.class).newInstance(userDaoInstance));
        } catch (Exception e) {
            throw new RuntimeException(e + " Bean 등록 시 예외가 발생했습니다.");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
            .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("해당하는 클래스가 존재하지 않습니다."));
    }
}
