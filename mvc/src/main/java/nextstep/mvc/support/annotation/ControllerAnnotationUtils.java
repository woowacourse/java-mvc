package nextstep.mvc.support.annotation;

import static nextstep.mvc.support.annotation.AnnotationHandleUtils.getClassesAnnotated;

import java.util.Set;
import nextstep.web.annotation.Controller;

public class ControllerAnnotationUtils {

    private ControllerAnnotationUtils() {
    }

    public static Set<Class<?>> findControllers(String basePackagePath) {
        return getClassesAnnotated(basePackagePath, Controller.class);
    }
}
