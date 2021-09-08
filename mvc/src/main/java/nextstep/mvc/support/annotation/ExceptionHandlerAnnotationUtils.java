package nextstep.mvc.support.annotation;

import static nextstep.mvc.support.annotation.AnnotationHandleUtils.getMethodsAnnotatedWith;

import java.lang.reflect.Method;
import java.util.List;
import nextstep.web.annotation.ExceptionHandler;

public class ExceptionHandlerAnnotationUtils {

    private ExceptionHandlerAnnotationUtils() {
    }

    public static List<Method> findByController(Class<?> controller) {
        return getMethodsAnnotatedWith(controller, ExceptionHandler.class);
    }

    public static Class<?> getHandleExceptionType(Method method) {
        return method.getAnnotation(ExceptionHandler.class).value();
    }

    public static boolean isMapped(Class<?> requestType, Method method) {
        Class<?> type = method.getAnnotation(ExceptionHandler.class).value();
        return type.equals(requestType);
    }
}
