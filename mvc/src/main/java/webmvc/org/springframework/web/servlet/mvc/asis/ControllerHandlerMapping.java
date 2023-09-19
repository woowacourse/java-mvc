package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.disapatchersevlet.HandlerKey;
import webmvc.org.springframework.web.servlet.mvc.disapatchersevlet.HandlerMapping;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class ControllerHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ControllerHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, Controller> controllerHandlers;

    public ControllerHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.controllerHandlers = new HashMap<>();
        controllerHandlers.put(new HandlerKey("/", RequestMethod.GET), new ForwardController("/index.jsp"));
    }

    @Override
    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<? extends Controller>> controllerClasses = reflections.getSubTypesOf(Controller.class);
        for (Class<? extends Controller> controllerClass : controllerClasses) {
            final Controller controller = getController(controllerClass);
            final String path = getPath(controllerClass);
            log.info("path={}", path);
            final List<HandlerKey> handlerKeys = getHandlerKeysAllRequestMethod(path);
            handlerKeys.forEach(handlerKey -> controllerHandlers.put(handlerKey, controller));
        }
        log.info("Initialized InterfaceHandlerMapping!");
    }

    private List<HandlerKey> getHandlerKeysAllRequestMethod(final String path) {
        return Arrays.stream(RequestMethod.values())
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .collect(Collectors.toList());
    }

    private String getPath(final Class<? extends Controller> controllerClass) {
        final String className = controllerClass.getSimpleName();
        final List<String> controllerPaths = new ArrayList<>();
        int lowerBound = 0;
        for (int i = 0; i < className.length(); i++) {
            final char charAt = className.charAt(i);
            if (charAt >= 'A' && charAt <= 'Z') {
                controllerPaths.add(className.substring(lowerBound, i).toLowerCase());
                lowerBound = i;
            }
        }
        return String.join("/", controllerPaths);
    }

    private Controller getController(final Class<? extends Controller> controllerClass) {
        try {
            final Constructor<? extends Controller> declaredConstructor = controllerClass.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("ControllerHandlerMapping 중 Controller를 찾을 수 없습니다.");
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey matchingHandlerKey = controllerHandlers.keySet().stream()
                .filter(handlerKey -> handlerKey.isMatching(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("404"));
        return controllerHandlers.get(matchingHandlerKey);
    }
}
