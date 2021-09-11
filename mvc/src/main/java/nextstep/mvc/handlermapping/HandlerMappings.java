package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.HandlerMapping;

public class HandlerMappings {

    private final List<HandlerMapping> elements;

    public HandlerMappings() {
        this(new ArrayList<>());
    }

    public HandlerMappings(final List<HandlerMapping> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public void initialize() {
        for (final HandlerMapping element : elements) {
            element.initialize();
        }
    }

    public void add(final HandlerMapping handlerMapping) {
        elements.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return elements.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("적절한 핸들러를 찾지 못 했습니다.(%s)(%s)", request.getRequestURI(), request.getMethod())));
    }
}
