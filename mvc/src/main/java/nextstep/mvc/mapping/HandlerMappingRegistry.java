package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.exeption.HandlerMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappingRegistry {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappingRegistry.class);

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this(new ArrayList<>());
    }

    public HandlerMappingRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = new ArrayList<>(handlerMappings);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void initialize() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Optional<Object> getHandlerMapping(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler instanceof HandlerExecution) {
                return Optional.of(handler);
            }
            return Optional.ofNullable(getManualControllerHandler(handlerMapping, request));
        }
        return Optional.empty();
    }

    private Object getManualControllerHandler(HandlerMapping handlerMapping, HttpServletRequest request) {
        try {
            final Optional<Controller> controller = findManualController(handlerMapping, request);
            if (controller.isEmpty()) {
                return null;
            }
            final Method execute = controller.get().getClass().getMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
            return new HandlerExecution(controller.get(), execute);
        } catch (NoSuchMethodException e) {
            log.info("해당 HTTP 메서드의 컨트롤러가 존재하지 않습니다. 이유: {}", e.getMessage());
            throw new HandlerMappingException("해당 HTTP 메서드의 컨트롤러가 존재하지 않습니다. 이유: " + e.getMessage());
        }
    }

    private Optional<Controller> findManualController(HandlerMapping handlerMapping, HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(it -> !it.equals(handlerMapping))
                .map(it -> it.getHandler(request))
                .filter(Objects::nonNull)
                .map(Controller.class::cast)
                .findFirst();
    }
}
