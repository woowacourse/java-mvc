package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.exception.HandlerNotFoundException;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> values;

    public HandlerMappingRegistry() {
        this.values = new ArrayList<>();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        values.add(handlerMapping);
        handlerMapping.initialize();
    }

    public Object getHandler(final HttpServletRequest request) {
        return values.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(HandlerNotFoundException::new);
    }
}
