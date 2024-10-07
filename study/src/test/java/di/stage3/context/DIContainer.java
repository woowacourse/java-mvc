package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            for (Class<?> anInterface : aClass.getInterfaces()) {
                beans.add(anInterface);
            }
            beans.add(aClass);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        try {
            if (beans.contains(aClass)) {
                // 인터페이스 일 경우 구현체를 찾아 인스턴스 반환
                if (aClass.isInterface()) {
                    System.out.println("인터페이스다:" + aClass.getName());
                    for (Object bean : beans) {
                        Class<?> bClass = (Class<?>) bean;
                        Class<?>[] interfaces = bClass.getInterfaces();
                        for (Class<?> anInterface : interfaces) {
                            if (anInterface.getName() == aClass.getName()) {
                                System.out.println("구현체 찾음");
                                return (T) getBean(bClass);
                            }
                        }
                    }
                }

                // 클래스 일 경우 인스턴스 반환
                for (Constructor<?> constructor : aClass.getConstructors()) {
                    int parameterCount = constructor.getParameterCount();
                    System.out.println(aClass.getName() + ", paramCount = " + parameterCount);
                    if (parameterCount == 0) {
                        return (T) constructor.newInstance();
                    }
                    // 파라미터타입들
                    Object[] classes = new Object[parameterCount];
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    for (int i = 0; i < parameterCount; i++) {
                        System.out.println(parameterTypes[i]);
                        classes[i] = getBean(parameterTypes[i]);
                    }
                    return (T) constructor.newInstance(classes);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
