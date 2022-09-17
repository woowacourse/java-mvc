package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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

        for (var basePackage : basePackages) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

            for( var clazz : classes){
                try {
                    Object controller = clazz.getDeclaredConstructor().newInstance();
                    for (var method : clazz.getMethods()){ // 클래스안의 메서드들
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            String url = requestMapping.value();
                            RequestMethod[] methods = requestMapping.method();
                            for (RequestMethod requestMethod : methods) {
                                log.info("SAVE >> url :{}, requestMethod:{}, method:{}", url, requestMethod, method);
                                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                                handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
                            }
                        }
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod httpMethod = RequestMethod.from(request.getMethod());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), httpMethod));
    }
}
