package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.tobe.exception.ControllerNotFoundException;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        List<String> packageNames = getPackageNames();

        List<ControllerPackage> controllerPackages = ControllerPackage.from(packageNames);
        List<ControllerClass> controllerClasses = getControllerClasses(controllerPackages);
        List<ControllerMethod> controllerMethodsOfClasses = getControllerMethods(controllerClasses);

        for (ControllerMethod controllerMethod : controllerMethodsOfClasses) {
            addHandlerExecutions(controllerMethod);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<String> getPackageNames() {
        return Arrays.stream(basePackage)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<ControllerClass> getControllerClasses(final List<ControllerPackage> controllerPackages) {
        return controllerPackages.stream()
                .flatMap(it -> ControllerClass.from(it).stream())
                .collect(Collectors.toList());
    }

    private List<ControllerMethod> getControllerMethods(final List<ControllerClass> controllerClasses) {
        return controllerClasses.stream()
                .flatMap(it -> ControllerMethod.from(it).stream())
                .collect(Collectors.toList());
    }

    private void addHandlerExecutions(final ControllerMethod controllerMethod) {
        List<HandlerKey> handlerKeys = HandlerKey.from(controllerMethod);
        HandlerExecution handlerExecution = new HandlerExecution(controllerMethod);

        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);

        if (handlerExecution == null) {
            throw new ControllerNotFoundException();
        }

        return handlerExecution;
    }
}
