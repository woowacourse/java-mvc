package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, AnnotationHandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        for (Object controller : controllerScanner.getAnnotatedControllerClasses()) {
            addAnnotatedHandlerExecution(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addAnnotatedHandlerExecution(Object controller) {
        for (Method method : getRequestMappedMethods(controller)) {
            HandlerKey handlerKey = createHandlerKey(method);
            AnnotationHandlerExecution annotationHandlerExecution = new AnnotationHandlerExecution(
                    controller, method);
            handlerExecutions.put(handlerKey, annotationHandlerExecution);
        }
    }

    private List<Method> getRequestMappedMethods(Object controller) {
        return Arrays.stream(controller.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private HandlerKey createHandlerKey(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
