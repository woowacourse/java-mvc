package nextstep.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappings {

    private final List<HandlerMapping> mappings;

    public HandlerMappings() {
        this(new ArrayList<>());
    }

    public HandlerMappings(HandlerMapping... mappings) {
        this(Arrays.asList(mappings));
    }

    public HandlerMappings(List<HandlerMapping> mappings) {
        this.mappings = new ArrayList<>(mappings);
    }

    public void initialize() {
        mappings.forEach(HandlerMapping::initialize);
    }

    public void add(HandlerMapping handlerMapping) {
        mappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return mappings.stream()
                       .map(handlerMapping -> handlerMapping.getHandler(request))
                       .filter(Objects::nonNull)
                       .findAny()
                       .orElseThrow(() -> new IllegalArgumentException("요청을 처리할 핸들러를 찾을 수 없습니다."));
    }
}
