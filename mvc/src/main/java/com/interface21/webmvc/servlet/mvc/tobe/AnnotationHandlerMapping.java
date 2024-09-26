package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<? extends RequestMapping> MAPPING_ANNOTATION = RequestMapping.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner scanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.scanner = new ControllerScanner(basePackage);
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        scanner.getAll()
                .forEach(this::initializeControllerHandlers);
    }

    private void initializeControllerHandlers(Class<?> clazz) {
        Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MAPPING_ANNOTATION))
                .forEach(method -> initializeHandler(method, scanner.get(clazz)));
    }

    private void initializeHandler(Method method, Object instance) {
        RequestMapping annotation = method.getAnnotation(MAPPING_ANNOTATION);
        RequestMethod[] requestMethods = annotation.method();
        String mappingUrl = annotation.value();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(mappingUrl, requestMethod);
            handlerExecutions.put(key, new HandlerExecution(method, instance));
        }
    }

    @Override
    public boolean hasHandler(HttpServletRequest request) {
        HandlerKey handlerKey = makeHandlerKeyByRequest(request);
        return handlerExecutions.containsKey(handlerKey);
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = makeHandlerKeyByRequest(request);
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new NoSuchElementException(handlerKey + "와 일치하는 핸들러 메소드가 없습니다");
        }
        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey makeHandlerKeyByRequest(HttpServletRequest request) {
        return new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
    }
}
