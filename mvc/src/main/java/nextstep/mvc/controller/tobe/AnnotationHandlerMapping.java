package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.exception.HandlerMappingInitiationException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    public void initialize() {
        log.info("Start Initialing AnnotationHandlerMapping!");

        for (Object basePackage : basePackages) {
            log.info("Register HandlerExecution from package: {}", basePackage);
            registerHandlerMapping(basePackage);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerMapping(Object packageName) {
        Reflections reflections = new Reflections(packageName);

        try {
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

            for (Class<?> controller : controllers) {
                List<Method> mappedMethods = getRequestMappingMethods(controller);

                Object instance = controller.getDeclaredConstructor().newInstance();

                for (Method mappedMethod : mappedMethods) {
                    RequestMapping requestMapping = mappedMethod.getAnnotation(RequestMapping.class);

                    String mappedUrl = requestMapping.value();
                    RequestMethod[] mappedHttpMethods = requestMapping.method();

                    for (RequestMethod mappedHttpMethod : mappedHttpMethods) {
                        HandlerKey handlerKey = new HandlerKey(mappedUrl, mappedHttpMethod);
                        HandlerExecution handlerExecution = new HandlerExecution(instance, mappedMethod);
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        } catch (InvocationTargetException |
                InstantiationException |
                IllegalAccessException |
                NoSuchMethodException exception) {
            throw new HandlerMappingInitiationException(exception);
        }
    }

    private List<Method> getRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> Stream.of(method.getAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(RequestMapping.class)))
                .collect(Collectors.toList());
    }
}
