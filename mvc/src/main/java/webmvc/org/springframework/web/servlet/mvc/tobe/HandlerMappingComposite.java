package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappingComposite {

    private final HandlerKeyDuplicateChecker handlerKeyDuplicateChecker = new HandlerKeyDuplicateChecker();
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(final HandlerMapping newHandlerMapping) {
        handlerKeyDuplicateChecker.checkWithStackedHandlerKeys(newHandlerMapping.getHandlerKeys());
        handlerMappings.add(newHandlerMapping);
    }

    public Optional<HandlerMapping> getHandlerMapping(final HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.support(request))
                .findFirst();
    }
}
