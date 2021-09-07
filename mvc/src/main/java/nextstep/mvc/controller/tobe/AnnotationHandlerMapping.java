package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.ControllerScanner;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        try {
            ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
            Map<Class<?>, Object> controllers = controllerScanner.getControllers();

            // TODO: 일급 컬렉션 분리
            for (Class<?> aClass : controllers.keySet()) {
                Set<Method> allMethods = ReflectionUtils.getAllMethods(aClass, ReflectionUtils.withAnnotation(RequestMapping.class));
                for (Method method : allMethods) {

                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    for (RequestMethod httpMethod : requestMapping.method()) {
                        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), httpMethod);
                        HandlerExecution handlerExecution = new HandlerExecution(method, controllers.get(aClass));
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }

                }
            }

            LOG.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
