package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import web.org.springframework.web.exception.HandlerMappingNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerMapping handlerMapping = getHandlerMapping(request);
        return handlerMapping.getHandler(request);
    }

    private HandlerMapping getHandlerMapping(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.supports(request))
                .findFirst()
                .orElseThrow(() -> new HandlerMappingNotFoundException("해당하는 핸들러 매핑이 없습니다."));
    }
}
