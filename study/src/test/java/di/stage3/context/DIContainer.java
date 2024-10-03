package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans = new HashSet<>();


    public DIContainer(Set<Class<?>> classes) {
        classes.forEach(this::registerBean);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) this.beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 빈입니다."));
    }

    private void registerBean(Class<?> clazz) {
        clazz = getImplementClass(clazz);
        Constructor<?> constructor = getConstructor(clazz);
        Parameter[] parameters = constructor.getParameters();

        if (parameters.length == 0) {
            beans.add(createInstance(clazz));
            return;
        }

        for (Parameter parameter : parameters) {
            Class<?> parameterType = parameter.getType();
            if (isNotRegistered(parameterType)) {
                registerBean(parameterType);
            }
        }
        beans.add(createInstanceWithInjection(constructor));
    }

    private Class<?> getImplementClass(Class<?> clazz) {
        if (clazz.isInterface()) {
            Reflections reflection = new Reflections(clazz.getPackageName());
            return reflection.getSubTypesOf(clazz).stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("구현 클래스가 존재하지 않습니다."));
        }
        return clazz;
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length > 1) {
            throw new IllegalStateException(clazz.getName() + "의 생성자가 1개 이상입니다.");
        }
        return constructors[0];
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("인스턴스 생성에 실패했습니다.");
        }
    }

    private boolean isNotRegistered(Class<?> parameterType) {
        return beans.stream().noneMatch(parameterType::isInstance);
    }

    private Object createInstanceWithInjection(Constructor<?> constructor) {
        Object[] args = getInjections(constructor);
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new IllegalStateException("인스턴스 생성에 실패했습니다.");
        }
    }

    private Object[] getInjections(Constructor<?> constructor) {
        Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Class<?> parameterType = parameters[i].getType();
            Object bean = getBean(parameterType);
            args[i] = bean;
        }
        return args;
    }
}
