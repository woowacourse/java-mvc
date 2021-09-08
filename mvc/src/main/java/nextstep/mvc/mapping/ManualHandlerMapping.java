package nextstep.mvc.mapping;

import java.util.HashMap;
import java.util.Map;

import air.annotation.Component;
import air.context.ApplicationContext;
import air.context.ApplicationContextProvider;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.configuration.HandlerConfigurer;
import nextstep.mvc.controller.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    private final ApplicationContext context = ApplicationContextProvider.getApplicationContext();

    @Override
    public void initialize() {
        HandlerConfigurer handlerConfigurer = context.findBeanByType(HandlerConfigurer.class);
        controllers.putAll(handlerConfigurer.customHandlerSetting());
        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                   .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }
}
