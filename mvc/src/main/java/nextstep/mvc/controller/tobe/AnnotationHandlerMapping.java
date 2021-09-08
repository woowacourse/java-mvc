package nextstep.mvc.controller.tobe;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.tobe.scanner.ControllerScanner;
import nextstep.mvc.controller.tobe.scanner.RequestMappingScanner;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    // todo 왜 Object 로 받을까?
    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object basePackage : basePackages) {
            String basePackageURL = (String) basePackage;

            Reflections reflections = createReflections(basePackageURL);

            ControllerScanner controllerScanner = new ControllerScanner(reflections);
            Map<Class<?>, Object> controllers = controllerScanner.getControllers();

            RequestMappingScanner requestMappingScanner = new RequestMappingScanner(reflections);
            handlerExecutions.putAll(requestMappingScanner.getHandlerExecutions(controllers));
        }
    }

    private Reflections createReflections(String basePackageURL) {
        return new Reflections(
                basePackageURL,
                new MethodAnnotationsScanner(),
                new TypeAnnotationsScanner(),
                new SubTypesScanner()
        );
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
