package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void initializeEach() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("맞는 핸들러가 없습니다.");
                });
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }
}
