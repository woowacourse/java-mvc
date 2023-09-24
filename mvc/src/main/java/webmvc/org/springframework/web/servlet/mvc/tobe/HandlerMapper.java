package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashSet;
import java.util.Set;

public class HandlerMapper {

    private final Set<HandlerMapping> handlerMappings = new HashSet<>();

    public HandlerMapper(HandlerMapping... handlerMappings) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
            this.handlerMappings.add(handlerMapping);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.isSupport(request)) {
                return handlerMapping.getHandler(request);
            }
        }

        throw new IllegalArgumentException("지원하지 않는 요청입니다");
    }
}
