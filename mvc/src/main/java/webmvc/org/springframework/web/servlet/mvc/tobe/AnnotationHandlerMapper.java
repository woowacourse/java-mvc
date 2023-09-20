package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapper;

import static java.util.stream.Collectors.toList;

public class AnnotationHandlerMapper implements HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapper.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapper(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.forEach(controller -> {
            Object handler = createHandlerByConstructor(controller);
            Arrays.stream(controller.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> parseMethod(handler, method));
        });
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void parseMethod(Object handler, Method method) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] httpMethods = annotation.method();
        final String path = annotation.value();
        addHandlerExecution(handler, method, httpMethods, path);
    }

    private void addHandlerExecution(Object handler, Method method, RequestMethod[] httpMethods, String path) {
        final List<HandlerKey> handlerKeys = Arrays.stream(httpMethods)
                .map(httpMethod -> new HandlerKey(path, httpMethod))
                .collect(toList());
        final HandlerExecution handlerExecution = new HandlerExecution(handler, method);
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private static Object createHandlerByConstructor(Class<?> controller) {
        try {
            Object handler = controller.getConstructor().newInstance();
            return handler;
        } catch (Exception e) {
            throw new IllegalArgumentException("해당 컨트롤러의 생성자로 객체를 생성할 수 없습니다." + controller.getSimpleName() + "의 생성자를 다시 확인해주세요.");
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

}
