package webmvc.org.springframework.web.servlet.mvc.tobe;


import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappings implements HandlerMapping<Object> {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    @Override
    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
            .map(it -> getHandler(request))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseGet(null);
    }
}
