package nextstep.mvc.support;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private ControllerScanner() {
    }

    public static Map<Class<?>, Object> getControllers(final Object... basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return controllers.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        ControllerScanner::initiateControllers
                ));
    }

    private static Object initiateControllers(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor()
                    .newInstance();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
