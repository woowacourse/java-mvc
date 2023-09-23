package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Set;

public class HandlerProcessor {

    private static final Set<HandlerMapping> handlerMappings = new HashSet<>();

    public static void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public HandlerProcessor(HandlerMapping... handlerMappings) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
            this.handlerMappings.add(handlerMapping);
        }
    }

    public Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.isSupport(request)) {
                return handlerMapping.executeHandler(request, response);
            }
        }

        throw new IllegalArgumentException("지원하지 않는 요청입니다");
    }
}
