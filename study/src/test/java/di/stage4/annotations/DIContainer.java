package di.stage4.annotations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);
    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = classes.stream()
                .map(this::makeInstance)
                .collect(Collectors.toSet());
        setBeans();
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> bean.getClass().equals(aClass))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Bean이 존재하지 않습니다."));
    }

    public Object makeInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            log.error("메서드를 찾지 못했습니다. {}", e.getStackTrace()[0]);
            throw new IllegalArgumentException("메서드를 찾지 못했습니다.");
        } catch (IllegalAccessException e) {
            log.error("생성자에 접근할 수 없습니다. {}", e.getStackTrace()[0]);
            throw new IllegalStateException("생성자에 접근할 수 없습니다.");
        } catch (ExceptionInInitializerError e) {
            log.error("메서드를 초기화 하던 도중 실패했습니다. {}", e.getStackTrace()[0]);
            throw new IllegalStateException("메서드를 초기화 하던 도중 실패했습니다.");
        } catch (InvocationTargetException | InstantiationException e) {
            log.error("생성자를 실행하던 도중 실패했습니다. {}", e.getStackTrace()[0]);
            throw new IllegalStateException("생성자를 실행하던 도중 실패했습니다.");
        }
    }

    private void setBeans() {
        for (Object bean : beans) {
            setFields(bean);
        }
    }

    private void setFields(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            field.setAccessible(true);

            setField(bean, field, fieldType);
        }
    }

    private void setField(Object bean, Field field, Class<?> fieldType) {
        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(matchedBean -> setToMatchedBean(bean, field, matchedBean));
    }

    private void setToMatchedBean(Object bean, Field field, Object matchedFieldBean) {
        try {
            field.set(bean, matchedFieldBean);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("해당 Field에 접근할 수 없습니다.");
        }
    }
}
