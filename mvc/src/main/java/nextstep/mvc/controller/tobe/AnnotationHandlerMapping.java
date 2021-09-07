package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Object> controllers = new HashSet<>();
        for (Class<?> controllerType : controllerTypes) {
            try {
                controllers.add(controllerType.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.info("AnnotationHandlerMapping#initialize, 인스턴스화를 할 수 없습니다.");
            }
        }

        for (Object controller : controllers) {
            Set<Method> methods = new HashSet<>();
            Method[] declaredMethods = controller.getClass().getDeclaredMethods();

            // controller에 선언되어있는 method들을 methods에 담는다.
            for (Method method : declaredMethods) {
                if (Arrays.stream(method.getDeclaredAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(RequestMapping.class))){
                    methods.add(method);
                }
            }

            // RequestMapping 어노테이션이 붙은 annotation의 uri와 value를 가져와서 handlerExecutions에 담는다.
            for (Method method: methods) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                String uri = annotation.value();

                for (RequestMethod requestMethod: annotation.method()) {
                    handlerExecutions.put(new HandlerKey(uri, requestMethod), new HandlerExecution(controller, method));
                }
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
