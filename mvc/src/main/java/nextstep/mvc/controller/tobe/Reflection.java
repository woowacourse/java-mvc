package nextstep.mvc.controller.tobe;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface Reflection {
    Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation);
}
