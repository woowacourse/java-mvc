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

            addHandlerExecutions(methods, controller);
        }
    }

    private void addHandlerExecutions(final List<Method> methods, final Class<?> controller) {
        for (final Method method : methods) {
            final RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
            final String path = requestMappingAnnotation.value();
            final RequestMethod requestMethod = requestMappingAnnotation.method()[0];
            final HandlerKey handlerKey = new HandlerKey(path, requestMethod);

            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
