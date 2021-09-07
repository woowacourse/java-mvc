package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.ControllerScanner;
import nextstep.web.annotation.Controller;
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
            
            LOG.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return null;
    }
}
