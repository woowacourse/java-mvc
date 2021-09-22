package nextstep.mvc.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.MvcComponentException;
import nextstep.mvc.handler.param.ArgumentResolver;
import nextstep.mvc.support.BeanNameParserUtils;
import nextstep.mvc.support.annotation.RequestMappingAnnotationUtils;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
            return invoke(request, response, handler);
        } catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getCause();
        }
    }

    private ModelAndView invoke(HttpServletRequest request, HttpServletResponse response, Method handler) throws IllegalAccessException, InvocationTargetException {
        Object[] arguments = ArgumentResolver.resolveRequestParam(controller, handler, request, response);
        return resultMapping(handler.invoke(controller, arguments));
    }

    private ModelAndView resultMapping(Object result) {
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }

        if (result instanceof String) {
            String viewName = (String) result;
            return new ModelAndView(viewName);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(BeanNameParserUtils.toLowerFirstChar(result.getClass().getSimpleName()), result);
        return modelAndView;
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
