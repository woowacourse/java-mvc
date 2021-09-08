package nextstep.mvc.handler.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import nextstep.mvc.exception.MvcComponentException;
import nextstep.mvc.handler.param.ArgumentResolver;
import nextstep.mvc.support.annotation.RequestMappingAnnotationUtils;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.RequestMethod;

public class HandlerExecution {

    private final Object controller;

    private HandlerExecution(Object controller) {
        this.controller = controller;
    }

    public static HandlerExecution of(Class<?> controller) {
        try {
            return new HandlerExecution(controller.getConstructor().newInstance());
        } catch (Exception e) {
            throw new MvcComponentException("적절한 컨트롤러가 아닙니다.");
        }
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        try {
            Method handler = findRequestHandler(request);
            Object[] arguments = ArgumentResolver.resolveRequestParam(controller, handler, request, response);

            return (ModelAndView) handler.invoke(controller, arguments);
        } catch (InvocationTargetException invocationTargetException){
            throw invocationTargetException.getCause();
        }
    }

    private Method findRequestHandler(HttpServletRequest request) {
        return RequestMappingAnnotationUtils.findByController(controller.getClass()).stream()
                .filter(method -> isRequestMapped(request, method))
                .findAny()
                .orElseThrow(() -> new MvcComponentException("적절한 HandlerExecution이 아닙니다."));
    }

    private boolean isRequestMapped(HttpServletRequest request, Method method) {
        String requestPath = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return RequestMappingAnnotationUtils.isMapped(method, requestPath, requestMethod);
    }
}
