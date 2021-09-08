package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.BeanScanner;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        List<Object> controllers = BeanScanner.getBeansWithAnnotation(basePackage, Controller.class);
        initializeHandlerExecutions(controllers);
    }

    private void initializeHandlerExecutions(List<Object> controllers) {
        try {
            for (Object controller : controllers) {
                List<Method> methodsWithAnnotation = Arrays.stream(controller.getClass().getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .collect(Collectors.toList());
                addHandlerExecutions(controller, methodsWithAnnotation);
            }
        } catch (Exception e) {
            log.error("Annotation Handler Mapping Fail!", e);
        }
    }

    private void addHandlerExecutions(Object controller, List<Method> methodsWithAnnotation) {
        for (Method method : methodsWithAnnotation) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            addHandlerExecutionsByKeys(controller, url, requestMethods, handlerExecution);
        }
    }

    private void addHandlerExecutionsByKeys(Object controller, String url, RequestMethod[] requestMethods, HandlerExecution handlerExecution) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            this.handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Path : {}, Controller : {}", handlerKey, controller);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(uri, requestMethod));
    }
}
