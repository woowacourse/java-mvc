package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.component.controllerscan.ReflectionLoader;
import nextstep.mvc.component.controllerscan.ReflectionsReflectionLoader;
import nextstep.mvc.exception.NotFoundException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object packageName : basePackage) {
            scanController((String) packageName);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void scanController(final String packageName) {
        final ReflectionLoader reflectionLoader = new ReflectionsReflectionLoader();
        final Set<Class<?>> controllers = reflectionLoader.getClassesAnnotatedWith(packageName, Controller.class);

        controllers.forEach(this::registerMappingHandler);
    }

    private void registerMappingHandler(final Class<?> controllerClass) {
        final Method[] declaredMethods = controllerClass.getDeclaredMethods();
        final List<Method> handlers = Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
        final Object controller = toInstance(controllerClass);
        handlers.forEach(handler -> requestMappingHandler(handler, controller));
    }

    private Object toInstance(final Class<?> controller) {
        try {
            final Constructor<?> constructor = controller.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException |
                NoSuchMethodException |
                InvocationTargetException |
                IllegalAccessException e) {
            throw new RuntimeException("컨트롤러 등록에 실패했습니다. 컨트롤러의 생성자를 확인하세요.", e);
        }
    }

    private void requestMappingHandler(final Method handler, final Object controller) {
        final RequestMapping requestMapping = handler.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(
                    new HandlerKey(requestMapping.value(), requestMethod),
                    new HandlerExecution(controller, handler)
            );
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new NotFoundException("매핑 정보가 없습니다.");
        }
        return handlerExecutions.get(handlerKey);
    }
}
