package nextstep.mvc.support;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.reflections.Reflections;

public class PackageScanner {

    private PackageScanner() {
    }

    public static Set<Class<?>> readTypesAnnotatedWith(final Class<? extends Annotation> annotation, final Object... classPath) {
        final Reflections reflections = new Reflections(classPath);
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
