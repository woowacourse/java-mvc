package di.stage3.context;

import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>(classes);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        try {
            if (beans.contains(aClass)) {
                if (aClass == InMemoryUserDao.class) {
                    return aClass.getConstructor().newInstance();
                }
                if (aClass == UserService.class) {
                    return aClass.getConstructor(UserDao.class).newInstance(getBean(InMemoryUserDao.class));
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
