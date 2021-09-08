package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.handler.mapping.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Set<Class<?>> handlerClasses = getHandlerClasses();
        putHandlerExecutionsOfHandlerClasses(handlerClasses);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void putHandlerExecutionsOfHandlerClasses(Set<Class<?>> handlerClasses) {
        for (Class<?> handlerClass : handlerClasses) {
            log.info("Annotation Controller 등록 : {}", handlerClass.getName());
            final Method[] methods = handlerClass.getDeclaredMethods();
            final Object handlerInstance = createHandlerInstance(handlerClass);
            putHandlerExecutionsOfHandlerMethods(handlerInstance, methods);
        }
    }

    private Object createHandlerInstance(Class<?> handlerClass) {
        try {
            return handlerClass.getConstructor().newInstance();
        } catch (Exception e) {
            log.error("핸들러 클래스 {} 의 인스턴스 생성 중 에러가 발생했습니다.", handlerClass.getName());
            throw new IllegalStateException(String.format("핸들러 클래스 %s 의 인스턴스 생성 중 에러가 발생했습니다.", handlerClass.getName()));
        }
    }

    private Set<Class<?>> getHandlerClasses() {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void putHandlerExecutionsOfHandlerMethods(Object handlerInstance, Method[] methods) {
        for (Method method : methods) {
            log.info("Annotation Controller {} 의 method {} 등록", handlerInstance.getClass().getName(), method.getName());
            final RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);

            final String requestUri = requestMappingAnnotation.value();
            final RequestMethod requestMethod = requestMappingAnnotation.method()[0];

            final HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(handlerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
