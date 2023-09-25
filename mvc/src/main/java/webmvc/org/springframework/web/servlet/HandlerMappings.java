package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.annotation.HandlerExecution;

import java.util.HashSet;
import java.util.Set;

public class HandlerMappings {

    private final Set<HandlerMapping> mappings;

    public HandlerMappings() {
        this.mappings = new HashSet<>();
    }

    public void init() {
        mappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        mappings.add(handlerMapping);
    }

    public HandlerExecution findHandlerExecution(HttpServletRequest request) {
        return mappings.stream()
                .filter(handlerMapping -> handlerMapping.support(request))
                .findFirst()
                .map(handlerMapping -> handlerMapping.getHandlerExecution(request))
                .orElseThrow(() -> new IllegalArgumentException("요청을 처리할 수 있는 핸들러가 없습니다."));
    }
}
