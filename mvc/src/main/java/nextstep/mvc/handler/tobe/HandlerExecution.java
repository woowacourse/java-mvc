package nextstep.mvc.handler.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
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
            throw new IllegalArgumentException("적절한 컨트롤러가 아닙니다.");
        }
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method handler = findRequestHandler(request);
        return (ModelAndView) handler.invoke(controller, request, response);
    }

    private Method findRequestHandler(HttpServletRequest request) {
        return RequestMappingAnnotationUtils.findByController(controller.getClass()).stream()
                .filter(method -> isRequestMapped(request, method))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("처리할 수 있는 handler가 없습니다."));
    }

    private boolean isRequestMapped(HttpServletRequest request, Method method) {
        String requestPath = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return RequestMappingAnnotationUtils.isMapped(method, requestPath, requestMethod);
    }
}
