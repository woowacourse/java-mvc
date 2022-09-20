package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.exception.NoDefaultConstructorException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        for (final Object basePackage : basePackages) {
            final Collection<URL> packagePath = ClasspathHelper.forPackage(basePackage.toString());
            final Reflections reflections = new Reflections(packagePath);
            final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

            mapControllerForHandlerExecution(controllers);
        }
    }

    private void mapControllerForHandlerExecution(final Set<Class<?>> controllers) {
        for (final Class<?> controller : controllers) {
            final List<Method> methods = Arrays.stream(controller.getDeclaredMethods())
                    .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toList());

            final Object controllerInstance = getControllerInstance(controller);
            mapMethodsForHandlerExecution(methods, controllerInstance);
        }
    }

    private Object getControllerInstance(final Class<?> controller) throws NoDefaultConstructorException {
        try {
            return controller.getConstructor().newInstance();
        } catch (final Exception e) {
            e.printStackTrace();
            throw new NoDefaultConstructorException();
        }
    }

    private void mapMethodsForHandlerExecution(final List<Method> methods, final Object controller) {
        for (final Method method : methods) {
            final RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
            final String path = requestMappingAnnotation.value();

            final RequestMethod[] requestMethods = requestMappingAnnotation.method();
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            addHandlerExecutionsByPath(path, requestMethods, handlerExecution);
        }
    }

    private void addHandlerExecutionsByPath(final String path, final RequestMethod[] requestMethods,
                                            final HandlerExecution handlerExecution) {
        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("[Add Handler Execution] handlerKey : {}, handlerExecution : {}", handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
