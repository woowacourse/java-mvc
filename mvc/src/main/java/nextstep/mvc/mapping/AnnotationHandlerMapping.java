package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.core.ApplicationContext;
import nextstep.mvc.annotation.Controller;
import nextstep.mvc.annotation.RequestMapping;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.mvc.controller.tobe.MethodHandler;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, MethodHandler> handlerExecutions;
    private final ApplicationContext applicationContext;

    public AnnotationHandlerMapping(ApplicationContext applicationContext) {
        this.handlerExecutions = new HashMap<>();
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        getControllerMethods();
    }

    private void getControllerMethods() {
        final List<Object> controllers =
            applicationContext.getBeansWithAnnotation(Controller.class);
        for (Object controller : controllers) {
            mapByHandlerKey(controller);
        }
    }

    private void mapByHandlerKey(Object controller) {
        for (Method method : controller.getClass().getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                final RequestMapping requestMapping =
                    method.getAnnotation(RequestMapping.class);
                final String uri = requestMapping.value();
                final RequestMethod[] methods = requestMapping.method();
                putEachHandlerKey(controller, method, uri, methods);
            }
        }
    }

    private void putEachHandlerKey(Object controller, Method method, String uri, RequestMethod[] methods) {
        for (RequestMethod requestMethod : methods) {
            this.handlerExecutions.put(new HandlerKey(uri, requestMethod), new MethodHandler(method,
                controller));
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod method = RequestMethod.of(request.getMethod());
        return handlerExecutions.get(new HandlerKey(url, method));
    }
}
