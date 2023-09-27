package webmvc.org.springframework.web.servlet.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = new ArrayList<>(handlerMappings);
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new HandlerMappingException("해당 요청에 대한 핸들러가 없습니다."));
    }
}
