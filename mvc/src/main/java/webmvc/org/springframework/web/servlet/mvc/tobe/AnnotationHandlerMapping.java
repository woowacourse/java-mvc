package webmvc.org.springframework.web.servlet.mvc.tobe;

import static java.util.Collections.emptyMap;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new ConcurrentHashMap<>();
    private final AnnotationScanner annotationScanner;
    private final HandlerKeyGenerator handlerKeyGenerator;
    private final AtomicBoolean initialized = new AtomicBoolean();

    public AnnotationHandlerMapping(final Object... basePackage) {
        this(emptyMap(), new AnnotationScanner(basePackage), new HandlerKeyGenerator());
    }

    public AnnotationHandlerMapping(
            Map<HandlerKey, HandlerExecution> handlerExecutions,
            AnnotationScanner annotationScanner,
            HandlerKeyGenerator handlerKeyGenerator
    ) {
        this.handlerExecutions.putAll(handlerExecutions);
        this.annotationScanner = annotationScanner;
        this.handlerKeyGenerator = handlerKeyGenerator;
    }

    public void initialize() {
        if (!initialized.compareAndSet(false, true)) {
            return;
        }

        final Map<Class<?>, ControllerInstance> controllers = annotationScanner.scanControllers();
        final Set<Method> methods = annotationScanner.scanHttpMappingMethods(controllers.keySet());
        for (final Method method : methods) {
            final ControllerInstance controller = controllers.get(method.getDeclaringClass());
            final HandlerExecution handlerExecution = new HandlerExecution(controller.getInstance(), method);
            final List<HandlerKey> handlerKeys = handlerKeyGenerator.generate(controller.getUriPrefix(), method);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }

        logHandlerExecutions();
    }

    private void logHandlerExecutions() {
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet().forEach(key -> {
            final HandlerExecution handler = handlerExecutions.get(key);
            log.info("key: {}, Class: {}, Method: {}", key, handler.getDeclaringClassName(), handler.getMethodName());
        });
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
