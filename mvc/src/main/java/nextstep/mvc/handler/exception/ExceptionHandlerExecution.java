package nextstep.mvc.handler.exception;

import java.lang.reflect.Method;
import nextstep.mvc.exception.MvcComponentException;
import nextstep.mvc.support.annotation.ExceptionHandlerAnnotationUtils;
import nextstep.mvc.view.ModelAndView;

public class ExceptionHandlerExecution {

    private final Object controller;

    private ExceptionHandlerExecution(Object controller) {
        this.controller = controller;
    }

    public static ExceptionHandlerExecution of(Class<?> controller) {
        try {
            return new ExceptionHandlerExecution(controller.getConstructor().newInstance());
        } catch (Exception e) {
            throw new MvcComponentException("적절한 컨트롤러가 아닙니다.");
        }
    }

    public ModelAndView handle(Exception exception) throws Exception {
        Method handler = findRequestHandler(exception);
        return (ModelAndView) handler.invoke(controller, exception);
    }

    private Method findRequestHandler(Exception exception) {
        return ExceptionHandlerAnnotationUtils.findByController(controller.getClass()).stream()
                .filter(method -> isExceptionMapped(exception, method))
                .findAny()
                .orElseThrow(() -> new MvcComponentException("예외를 처리할 수 있는 핸들러가 아닙니다."));
    }

    private boolean isExceptionMapped(Exception exception, Method method) {
        return ExceptionHandlerAnnotationUtils.isMapped(exception.getClass(), method);
    }
}
