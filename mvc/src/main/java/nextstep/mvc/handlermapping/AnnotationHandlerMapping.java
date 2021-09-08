package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import nextstep.mvc.controller.tobe.ControllerScanner;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final ControllerScanner controllerScanner = new ControllerScanner();
        for (Object onePackage : basePackage) {
            final Map<HandlerKey, HandlerExecution> onePackageMap = controllerScanner.scannedControllerMethod(
                onePackage);
            insertControllerMethodToAll(onePackageMap);
        }
    }

    private void insertControllerMethodToAll(Map<HandlerKey, HandlerExecution> onePackageMap) {
        handlerExecutions.putAll(onePackageMap);
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
