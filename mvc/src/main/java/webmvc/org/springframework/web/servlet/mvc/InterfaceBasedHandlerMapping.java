package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.annotation.HandlerExecution;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InterfaceBasedHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(InterfaceBasedHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    public static void addController(String path, Controller controller) {
        controllers.put(path, controller);
    }

    @Override
    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public boolean support(HttpServletRequest request) {
        return controllers.containsKey(request.getRequestURI());
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        log.debug("Request Mapping Uri : {}", request.getRequestURI());

        Method execute;
        try {
            execute = Controller.class.getDeclaredMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        Controller controller = controllers.get(request.getRequestURI());

        if (controller != null) {
            return new HandlerExecution(controller, execute);
        }
        return null;
    }
}
