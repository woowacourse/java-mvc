package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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

    public void initialize() {
        try {
            // TODO : basePackage에서 @Controller 어노테이션 붙은 클래스 전부 불러오기
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

            // TODO : @Controller 붙은 클래스에서 @RequestMapping 붙은 메서드들 전부 추출
            for (Class<?> controllerClass : controllerClasses) {
                List<Method> methods = Arrays.stream(controllerClass.getMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toList());

                // TODO : @RequestMapping의 value와 method 값을 얻어서 HandlerKey 객체 만들기
                for (Method method : methods) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String requestUri = requestMapping.value();
                    RequestMethod[] requestMethods = requestMapping.method();
                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
                        // TODO : Reflection을 활용해서 클래스의 instance와 method를 생성
                        Object instance = controllerClass.getConstructor().newInstance();

                        // TODO : HandlerExecution 만들기
                        HandlerExecution handlerExecution = new HandlerExecution(instance, method);

                        // TODO : handlerExecutions Map에 넣기
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, method));
    }
}
