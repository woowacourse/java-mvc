package nextstep.mvc.support.annotation;

import static nextstep.mvc.support.annotation.AnnotationHandleUtils.getClassesAnnotatedWith;

import java.util.Set;
import nextstep.web.annotation.Controller;

public class ControllerAnnotationUtils {

    private ControllerAnnotationUtils() {
    }

    public static Set<Class<?>> findControllers(String basePackagePath) {
        return getClassesAnnotatedWith(basePackagePath, Controller.class);
    }
}
