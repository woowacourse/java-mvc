package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HandlerMappingComposite implements HandlerMapping {

    private final Set<HandlerMapping> handlerMappings = new HashSet<>();

    public HandlerMappingComposite(HandlerMapping... handlerMappings) {
        this.handlerMappings.add(new AnnotationHandlerMapping());
        this.handlerMappings.addAll(Arrays.asList(handlerMappings));
    }

    @Override
    public void initialize() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }
}
