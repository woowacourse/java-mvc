package nextstep.mvc.handler.exception;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.mvc.exception.MvcComponentException;
import nextstep.mvc.support.annotation.ControllerAnnotationUtils;
import nextstep.mvc.support.annotation.ExceptionHandlerAnnotationUtils;
import nextstep.mvc.view.ModelAndView;

public class ExceptionHandlerExecutor {

    private final Map<Class<?>, ExceptionHandlerExecution> handlerExecutions;

    public ExceptionHandlerExecutor(String... basePackages) {
        this.handlerExecutions = new HashMap<>();
        Arrays.stream(basePackages).forEach(this::register);
    }

    private void register(String path) {
        for (Class<?> controller : ControllerAnnotationUtils.findControllers(path)) {
            ExceptionHandlerExecution handler = ExceptionHandlerExecution.of(controller);
            List<Method> methods = ExceptionHandlerAnnotationUtils.findByController(controller);
            methods.forEach(method -> registerHandler(method, handler));
        }
    }

    public ModelAndView execute(Exception exception) throws Exception {
        if (handlerExecutions.containsKey(exception.getClass())) {
            ExceptionHandlerExecution handlerExecution = (ExceptionHandlerExecution) getHandler(exception.getClass());
            return handlerExecution.handle(exception);
        }
        throw exception;
    }

    private Object getHandler(Class<?> exceptionType) {
        return handlerExecutions.get(exceptionType);
    }

    private void registerHandler(Method method, ExceptionHandlerExecution handler) {
        Class<?> exceptionType = ExceptionHandlerAnnotationUtils.getHandleExceptionType(method);
        if (handlerExecutions.containsKey(exceptionType)) {
            throw new MvcComponentException("중복 정의된 Exception Handler 입니다.");
        }
        handlerExecutions.putIfAbsent(exceptionType, handler);
    }
}
