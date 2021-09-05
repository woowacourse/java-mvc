package nextstep.mvc.support;

import static nextstep.mvc.support.AnnotationHandlerUtils.getClassesAnnotatedWith;

import java.util.Set;
import nextstep.web.annotation.Controller;

public class ControllerMappingHandlerUtils {

    private ControllerMappingHandlerUtils() {
    }

    public static Set<Class<?>> findControllers(String basePackagePath) {
        return getClassesAnnotatedWith(basePackagePath, Controller.class);
    }
}
