package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object basePackage : basePackages) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controller : controllers) {
                Object findController;
                try {
                    findController = controller.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException("기본 생성자가 존재하지 않습니다.");
                }
                Method[] declaredMethods = controller.getDeclaredMethods();
                Method[] requestMappingMethods = Arrays.stream(declaredMethods)
                        .filter(declaredMethod -> declaredMethod.isAnnotationPresent(RequestMapping.class))
                        .toArray(Method[]::new);
                for (Method method : requestMappingMethods) {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    final String url = annotation.value();
                    RequestMethod[] requestMethods;
                    if (annotation.method().length == 0) {
                        requestMethods = RequestMethod.values();
                    } else {
                        requestMethods = annotation.method();
                    }
                    for (RequestMethod requestMethod : requestMethods) {
                        log.info("등록 완료 : url ={}, method = {}, handlerMethod = {}", url, requestMethod.name(),
                                 method.getName()
                        );
                        handlerExecutions.put(
                                new HandlerKey(url, requestMethod),
                                new HandlerExecution(findController, method)
                        );
                    }
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return null;
    }
}
