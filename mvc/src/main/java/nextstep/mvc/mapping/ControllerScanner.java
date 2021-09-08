package nextstep.mvc.mapping;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private ControllerScanner() {
    }

    public static List<Object> getControllers(Object[] basePackage) {
        log.info("Scan Controllers In {} !", basePackage);
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        List<ControllerDefinition> controllerDefinitions = instantiateControllers(controllerClasses);
        return controllerDefinitions.stream().map(ControllerDefinition::getController).collect(Collectors.toList());
    }

    private static List<ControllerDefinition> instantiateControllers(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .map(ControllerDefinition::new)
                .collect(Collectors.toList());
    }
}
