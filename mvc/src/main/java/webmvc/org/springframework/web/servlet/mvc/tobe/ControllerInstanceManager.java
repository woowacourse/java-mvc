package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ControllerInstanceManager {

    private final Map<Class<?>, Object> instances = new HashMap<>();

    public Object getInstanceOf(Class<?> clazz) {
        if (instances.get(clazz) == null) {
            Object newInstance = safeInstantiate(clazz);
            instances.put(clazz, newInstance);
            return newInstance;
        }
        return instances.get(clazz);
    }

    public Object getInstanceOf(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return getInstanceOf(declaringClass);
    }

    private Object safeInstantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException exception) {
            throw new IllegalArgumentException("Class " + clazz.getSimpleName() + "에 기본 생성자가 없습니다");
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Class " + clazz.getSimpleName() + "초기화에 실패했습니다");
        }
    }
}
