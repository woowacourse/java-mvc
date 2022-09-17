package nextstep.mvc.component.controllerscan;

import java.lang.annotation.Annotation;
import java.util.Set;
import org.reflections.Reflections;

public class ReflectionsReflectionLoader implements ReflectionLoader {

    @Override
    public Set<Class<?>> getClassesAnnotatedWith(final String basePackage, final Class<? extends Annotation> annotation) {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
