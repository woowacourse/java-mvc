package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private final Object handler;

    public HandlerExecution(Object handler) {
        this.handler = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Method method = findMethodToHandle(request);
        return (ModelAndView) method.invoke(handler, request, response);
    }

    private Method findMethodToHandle(HttpServletRequest request) {
        Method[] methods = this.handler.getClass()
                .getDeclaredMethods();

        return Arrays.stream(methods)
                .filter(method -> canHandleRequest(method, request))
                .findFirst()
                .orElseThrow(RequestMappingNotFoundException::new);
    }

    private boolean canHandleRequest(Method method, HttpServletRequest request) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            return isRequestAndRequestMappingMatches(request, requestMapping);
        }
        return false;
    }

    private boolean isRequestAndRequestMappingMatches(HttpServletRequest request, RequestMapping requestMapping) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        return requestMapping.value().equals(requestURI) &&
                isHttpMethodSupported(requestMapping.method(), requestMethod);
    }

    private boolean isHttpMethodSupported(RequestMethod[] supportedMethods, String httpMethod) {
        return Arrays.stream(supportedMethods)
                .anyMatch(requestMethod -> requestMethod.hasValue(httpMethod));
    }
}
