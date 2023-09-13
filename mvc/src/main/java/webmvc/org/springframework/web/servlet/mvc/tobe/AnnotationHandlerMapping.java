package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    // HandlerKey는 url과 RequestMethod(GET, POST, PUT, DELETE, PATCH, ...)를 가지고 있음
    // HandlerExecution은 메서드 실행하는 것인듯?

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controller : controllers) {
            // HandlerKey 만들기
            final Method[] declaredMethods = controller.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                final RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);
                final String url = requestMapping.value();
                final RequestMethod[] requestMethods = requestMapping.method();
                for (RequestMethod requestMethod : requestMethods) {
                    final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return null;
    }
}
