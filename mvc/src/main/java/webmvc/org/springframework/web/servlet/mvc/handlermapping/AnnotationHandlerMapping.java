package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerKey;
import webmvc.org.springframework.web.servlet.view.ViewAdapter;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final String DEFAULT_REQUEST_URL = "";

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object path : basePackage) {
            Set<Class<?>> controllers = new Reflections(path).getTypesAnnotatedWith(Controller.class);
            initializeHandlerExecutions(controllers);
        }
    }

    private void initializeHandlerExecutions(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            preProcessRequestMappingInfo(controller, getRequestMappingInfo(controller));
        }
    }

    private void preProcessRequestMappingInfo(Class<?> controller, RequestMappingInfo controllerLevelRequestMappingInfo) {
        for (Method method : controller.getDeclaredMethods()) {
            RequestMappingInfo methodLevelRequestMappingInfo = getRequestMappingInfo(method);

            String totalUrl = controllerLevelRequestMappingInfo.value + methodLevelRequestMappingInfo.value;
            RequestMethod[] decidedMethods = decideRequestMethods(
                    controllerLevelRequestMappingInfo.methods,
                    methodLevelRequestMappingInfo.methods
            );
            addToHandlerExecutions(method, decidedMethods, totalUrl);
        }
    }

    private RequestMappingInfo getRequestMappingInfo(AnnotatedElement element) {
        if (element.isAnnotationPresent(RequestMapping.class)) {
            var requestMapping = element.getDeclaredAnnotation(RequestMapping.class);
            return new RequestMappingInfo(requestMapping.value(), requestMapping.method());
        }
        return new RequestMappingInfo(DEFAULT_REQUEST_URL, new RequestMethod[]{});
    }

    private RequestMethod[] decideRequestMethods(RequestMethod[] classLevels, RequestMethod[] methodLevels) {
        if (methodLevels.length == 0) {
            return classLevels;
        }
        return methodLevels;
    }

    private void addToHandlerExecutions(Method method, RequestMethod[] decidedRequestMethods, String totalRequestUrl) {
        for (RequestMethod requestMethod : decidedRequestMethods) {
            HandlerKey handlerKey = new HandlerKey(totalRequestUrl, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(new ViewAdapter(), method));
        }
    }

    @Override
    public Optional<Object> getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        return Optional.ofNullable(handlerExecutions.get(handlerKey));
    }

    private static class RequestMappingInfo {

        private final String value;
        private final RequestMethod[] methods;

        public RequestMappingInfo(String value, RequestMethod[] methods) {
            this.value = value;
            this.methods = methods;
        }
    }
}
