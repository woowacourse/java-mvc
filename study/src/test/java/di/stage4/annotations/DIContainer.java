package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DIContainer {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    private DIContainer(final Set<Class<?>> classes) {
        createBeans(classes);
        beans.values()
                .forEach(this::injectDependencies);
    }

    private void createBeans(final Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class)) {
                try {
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Object instance = constructor.newInstance();
                    beans.put(clazz, instance);

                    for (Class<?> iface : clazz.getInterfaces()) {
                        beans.put(iface, instance);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("의존성 주입 실패: " + clazz.getName(), e);
                }
            }
        }
    }

    private void injectDependencies(final Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                Class<?> fieldType = field.getType();
                Object dependency = beans.get(fieldType);
                if (dependency == null) {
                    throw new RuntimeException("그런 bean은 이 세상에 존재하지 않습니다: " + fieldType.getName());
                }
                field.setAccessible(true);
                try {
                    field.set(bean, dependency);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("의존성 주입 실패!: " + fieldType.getName(), e);
                }
            }
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beans.get(clazz));
    }

    public static DIContainer createContainerForPackage(String packageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(packageName);
        return new DIContainer(classes);
    }
}
