package webmvc.org.springframework.web.servlet.mvc.tobe.handler;


import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotFoundException;

public class HandlerMappingComposite implements HandlerMapping<Object> {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingComposite(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    @Override
    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
            .map(it -> it.getHandler(request))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(HandlerNotFoundException::new);
    }
}
