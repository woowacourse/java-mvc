package nextstep.mvc.component.controllerscan;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ReflectionLoader {

    Set<Class<?>> getClassesAnnotatedWith(final String basePackage, final Class<? extends Annotation> annotation);
}
