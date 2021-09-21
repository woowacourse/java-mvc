package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) throws NotFoundException {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new NotFoundException("핸들러 찾을 수 없음."));
    }
}
