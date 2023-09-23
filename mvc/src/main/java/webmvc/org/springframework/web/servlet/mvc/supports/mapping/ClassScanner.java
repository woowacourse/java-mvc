package webmvc.org.springframework.web.servlet.mvc.supports.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassScanner {

    private static final Logger log = LoggerFactory.getLogger(ClassScanner.class);

    private final Reflections reflections;

    public ClassScanner(final Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public List<Object> findInstanceByAnnotation(final Class<? extends Annotation> targetAnnotation) {
        final Set<Class<?>> targetClasses = reflections.getTypesAnnotatedWith(targetAnnotation);

        return targetClasses.stream()
                            .map(this::createInstanceOf)
                            .collect(Collectors.toList());
    }

    private Object createInstanceOf(final Class<?> targetClass) {
        try {
            return targetClass.getConstructor()
                              .newInstance();
        } catch (final NoSuchMethodException |
                InvocationTargetException |
                InstantiationException |
                IllegalAccessException e) {
            log.error("", e);

            throw new IllegalArgumentException("해당 클래스의 인스턴스를 생성할 수 없습니다.");
        }
    }
}
