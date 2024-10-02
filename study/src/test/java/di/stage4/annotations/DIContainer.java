package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Object> beans) {
        this.beans = beans;
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        Set<Object> beans = createBeans(allClassesInPackage);
        beans.forEach(bean -> injectDependency(bean, beans));

        return new DIContainer(beans);
    }

    private static Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(clazz -> {
                    try {
                        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
                        declaredConstructor.setAccessible(true);
                        return declaredConstructor.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("빈 객체 생성 실패", e);
                    }
                })
                .collect(Collectors.toSet());
    }

    private static void injectDependency(Object target, Set<Object> beans) {
        try {
            for (Field field : target.getClass().getDeclaredFields()) {
                if (isStaticField(field)) {
                    continue;
                }
                Object dependencyBean = getBean(field.getType(), beans);
                if (!isBeanDependency(dependencyBean)) {
                    continue;
                }

                field.setAccessible(true);
                field.set(target, dependencyBean);
            }
        } catch (Exception e) {
            throw new RuntimeException("의존성 주입 중 문제가 발생하였습니다.", e);
        }
    }

    private static boolean isStaticField(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    private static boolean isBeanDependency(Object dependencyBean) {
        return dependencyBean != null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getBean(final Class<T> aClass, Set<Object> beans) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElse(null);
    }
}
