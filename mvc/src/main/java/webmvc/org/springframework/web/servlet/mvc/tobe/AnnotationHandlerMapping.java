package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) throws Exception {
        this.basePackage = null;
        this.handlerExecutions = new HashMap<>();

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                if (annotation != null) {
                    for (RequestMethod requestMethod : annotation.method()) {
                        HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
                        Object o = aClass.getConstructor().newInstance();
                        HandlerExecution handlerExecution = new HandlerExecution() {
                            @Override
                            public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
                                    throws Exception {
                                return (ModelAndView) method.invoke(o, request, response);
                            }
                        };
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
