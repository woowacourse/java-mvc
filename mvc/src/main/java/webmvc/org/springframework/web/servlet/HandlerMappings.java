package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.InterfaceBasedHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.annotation.AnnotationBasedHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.annotation.HandlerExecution;

import java.util.HashSet;
import java.util.Set;

public class HandlerMappings {

    private Set<HandlerMapping> mappings;

    public void init() {
        mappings = new HashSet<>();

        HandlerMapping annotationBasedHandlerMapping = new AnnotationBasedHandlerMapping("java");
        annotationBasedHandlerMapping.initialize();
        mappings.add(annotationBasedHandlerMapping);

        InterfaceBasedHandlerMapping interfaceBasedHandlerMapping = new InterfaceBasedHandlerMapping();
        interfaceBasedHandlerMapping.initialize();
        mappings.add(interfaceBasedHandlerMapping);
    }

    public HandlerExecution findHandler(HttpServletRequest request) {
        return mappings.stream()
                .filter(handlerMapping -> handlerMapping.support(request))
                .findFirst()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .orElseThrow(() -> new IllegalArgumentException("요청을 처리할 수 있는 핸들러가 없습니다."));
    }
}
