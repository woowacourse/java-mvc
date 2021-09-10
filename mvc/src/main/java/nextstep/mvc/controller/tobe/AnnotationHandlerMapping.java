package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Handler;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(basePackage);

        // TODO: @Controller 붙어 있는 클래스들 가져오기
        Set<Class<?>> classesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);

        // TODO: 각 Controller 클래스마다 @RequestMapping 붙어 있는 메서드 가져오기
        for (Class<?> controllerClass : classesAnnotatedWithController) {
            Method[] declaredMethods = controllerClass.getDeclaredMethods();
            List<Method> methodsAnnotatedWithRequestMapping = Arrays.stream(declaredMethods)
                                                                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                                                                    .collect(Collectors.toList());

            // TODO: @RequestMapping 붙어 있는 각 메서드마다 request Url, HTTP METHOD(S) 가져오기
            for (Method method : methodsAnnotatedWithRequestMapping) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                String requestUri = annotation.value();
                RequestMethod[] requestMethods = annotation.method();

                // TODO: HandlerExecutions 채워넣기
                for (RequestMethod requestMethod : requestMethods) {
                    HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
                    Object handler = controllerClass.getConstructor().newInstance();
                    // handler가 사실상 controller
                    handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        log.debug("Request Mapping Uri : {} , Request Method : {}", requestUri, requestMethod);

        HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
