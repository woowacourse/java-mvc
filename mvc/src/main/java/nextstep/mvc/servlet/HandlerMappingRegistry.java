package nextstep.mvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappingRegistry.class);

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public void initHandlerMappings() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                handlerMapping.initialize();
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        Object handler = handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        log.info("handler : {}", handler.getClass().getName());
        return handler;
    }
}
