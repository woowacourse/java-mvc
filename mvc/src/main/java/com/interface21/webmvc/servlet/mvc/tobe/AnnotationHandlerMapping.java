package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements Handler {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllerClasses) {
            addAnnotatedHandlerExecutions(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addAnnotatedHandlerExecutions(Class<?> controller) {
        List<Method> annotatedMethods = searchRequestMappingAnnotation(controller);
        annotatedMethods
                .forEach(method -> handlerExecutions.addHandlerExecution(controller, method));
    }

    private List<Method> searchRequestMappingAnnotation(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(),
                RequestMethod.findMethod(request.getMethod()));
        return handlerExecutions.get(key);
    }

    @Override
    public boolean canHandle(HttpServletRequest request) {
        return getHandler(request) != null;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecution handler = (HandlerExecution) getHandler(request);
        ModelAndView modelAndView = handler.handle(request, response);
        modelAndView.render(request, response);
    }
}
