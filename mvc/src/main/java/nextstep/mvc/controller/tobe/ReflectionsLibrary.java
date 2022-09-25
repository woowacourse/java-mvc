package nextstep.mvc.controller.tobe;

import java.lang.annotation.Annotation;
import java.util.Set;
import org.reflections.Reflections;

public class ReflectionsLibrary implements Reflection{

    private Reflections reflections;

    public ReflectionsLibrary(Object[] targetPackage) {
        this.reflections = new Reflections(targetPackage);
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
