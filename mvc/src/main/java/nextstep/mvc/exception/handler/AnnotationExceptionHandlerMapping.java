package nextstep.mvc.exception.handler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.mvc.support.annotation.ControllerAnnotationUtils;
import nextstep.mvc.support.annotation.ExceptionHandlerAnnotationUtils;

public class AnnotationExceptionHandlerMapping {

    private final Map<Class<?>, ExceptionHandlerExecution> handlerExecutions;

    public AnnotationExceptionHandlerMapping(String... basePackages) {
        this.handlerExecutions = new HashMap<>();
        Arrays.stream(basePackages).forEach(this::register);
    }

    public Object getHandler(Class<?> exceptionType) {
        return handlerExecutions.get(exceptionType);
    }

    private void register(String path) {
        for (Class<?> controller : ControllerAnnotationUtils.findControllers(path)) {
            ExceptionHandlerExecution handler = ExceptionHandlerExecution.of(controller);
            List<Method> methods = ExceptionHandlerAnnotationUtils.findByController(controller);
            methods.forEach(method -> registerHandler(method, handler));
        }
    }

    private void registerHandler(Method method, ExceptionHandlerExecution handler) {
        Class<?> exceptionType = ExceptionHandlerAnnotationUtils.getHandleExceptionType(method);
        if (handlerExecutions.containsKey(exceptionType)) {
            throw new IllegalArgumentException("중복 정의된 Exception Handler 입니다.");
        }
        handlerExecutions.putIfAbsent(exceptionType, handler);
    }
}
