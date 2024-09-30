package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan.Filter;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        initializeBeans(classes);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    private void initializeBeans(final Set<Class<?>> classes) {
        constructBeans(classes);
        beans.forEach(this::injectDependencies);
    }

    private void constructBeans(final Set<Class<?>> classes) {
        classes.forEach(aClass -> {
            try {
                Constructor<?> constructor = aClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                beans.add(constructor.newInstance());
            } catch (Exception e) {
                log.error("빈 생성에서 문제 발생: {}", e.getMessage());
                throw new RuntimeException("빈 생성에서 문제 발생: " + e.getMessage(), e);
            }
        });
    }

    private void injectDependencies(final Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .forEach(field -> {
                try {
                    field.setAccessible(true);
                    field.set(bean, getBean(field.getType()));
                } catch (Exception e) {
                    log.warn("빈 의존성 주입에서 문제 발생: {}", e.getMessage());
                }
            });
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
            .filter(aClass::isInstance)
            .findAny()
            .orElseThrow(() -> new NoSuchElementException(aClass.getName() + " 클래스의 빈이 없음"));
    }
}
