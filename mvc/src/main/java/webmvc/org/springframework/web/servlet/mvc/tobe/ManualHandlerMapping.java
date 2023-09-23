package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.ApplicationContext;
import webmvc.org.springframework.web.servlet.mvc.ApplicationContextAware;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

public class ManualHandlerMapping extends ApplicationContextAware implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);
    private final Map<String, Controller> controllers;

    public ManualHandlerMapping() {
        controllers = new HashMap<>();
    }

    @Override
    public void initialize() {
        final ApplicationContext applicationContext = getApplicationContext();

        applicationContext.getBeansOfType(Controller.class)
            .stream()
            .map(Controller.class::cast)
            .forEach(controller -> controllers.put(makePath(controller), controller));
        controllers.put("/", (Controller) applicationContext.getBean(ForwardController.class));

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
            .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    private String makePath(final Controller controller) {
        final String className = controller.getClass().getSimpleName();
        final String controllerName = className.substring(0, className.indexOf("Controller"));
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < controllerName.length(); i++) {
            if (Character.isUpperCase(controllerName.charAt(i))) {
                stringBuilder.append("/").append(Character.toLowerCase(controllerName.charAt(i)));
            } else {
                stringBuilder.append(controllerName.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }
}
