package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.scanner.ControllerScanner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final AnnotationHandlerKeyComposite annotationHandlerKeyComposite;
    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final AnnotationHandlerKeyComposite annotationHandlerKeyComposite, final Object... basePackage) {
        this.annotationHandlerKeyComposite = annotationHandlerKeyComposite;
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        initialize();
    }

    @Override
    public void initialize() {
        new ControllerScanner()
                .getControllers(basePackage)
                .forEach(this::putHandlerExecutions);

        log.info("Initialized Annotation Handler Mapping!");
        handlerExecutions.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, handlerExecutions.get(path)));
    }

    @Override
    public boolean support(final HttpServletRequest request) {
        return handlerExecutions.containsKey(getHandlerKey(request));
    }

    private void putHandlerExecutions(final Class<?> clazz, final Object controller) {
        final List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .collect(Collectors.toList());

        for (final Method method : methods) {
            annotationHandlerKeyComposite.getHandlerKey(method)
                    .ifPresent(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(controller, method)));
        }
    }

    @Override
    public Object getHandlerExecution(final HttpServletRequest request) {
        return handlerExecutions.get(getHandlerKey(request));
    }

    @Override
    public List<HandlerKey> getHandlerKeys() {
        return new ArrayList<>(handlerExecutions.keySet());
    }
}
