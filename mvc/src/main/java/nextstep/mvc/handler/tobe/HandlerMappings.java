package nextstep.mvc.handler.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.exception.UnHandledRequestException;

public class HandlerMappings {
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(()-> new UnHandledRequestException("처리할 수 있는 핸들러가 없습니다."));
    }
}
