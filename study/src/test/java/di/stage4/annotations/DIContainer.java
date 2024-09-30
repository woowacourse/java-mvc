package di.stage4.annotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>(classes);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        Set<Class<?>> beanz = new HashSet<>();

        classes.stream()
                .map(clazz -> clazz.getDeclaredFields())
                .flatMap(Arrays::stream)
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> beanz.add(field.getType()));

        return new DIContainer(beanz);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        if (!beans.contains(aClass)) {
            return null;
        }

        Reflections reflections = new Reflections(getClass().getPackage().getName());
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        // aClass의 annotation이 @Repository
        if (repositoryClasses.contains(aClass)) {
            return new
        }

        // aClass

        return null;
    }
}
