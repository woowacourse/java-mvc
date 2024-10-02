package di.stage4.annotations;

import di.ConsumerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(di.stage4.annotations.DIContainer.class);
    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        instantiateClasses(classes);
        setFields(beans);
    }

    public static DIContainer createContainerForPackage(String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        Set<Class<?>> setClasses = classes.stream()
                .filter(aClass -> aClass.isAnnotationPresent(Service.class)
                        || aClass.isAnnotationPresent(Repository.class))
                .collect(Collectors.toSet());

        return new DIContainer(setClasses);
    }

    private void instantiateClasses(Set<Class<?>> classes) {
        classes.stream()
                .map(this::instantiateClass)
                .forEach(beans::add);
    }

    private Object instantiateClass(Class<?> aClass) {
        try {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException exception) {
            log.error("생성자를 호출할 수 없습니다.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            log.error("인스턴스를 생성할 수 없습니다.");
        }

        return null;
    }

    private void setFields(Set<Object> beans) {
        for (Object bean : beans) {
            Arrays.stream(bean.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .forEach(field -> setField(bean, field));
        }
    }

    private void setField(Object bean, Field field) {
        Class<?> fieldType = field.getType();
        field.setAccessible(true);

        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(ConsumerWrapper.accept(matchBean -> field.set(bean, matchBean)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 빈입니다."));
    }
}
