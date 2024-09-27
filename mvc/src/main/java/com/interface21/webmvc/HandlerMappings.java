package com.interface21.webmvc;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public HandlerMapping getHandlerMapping(String url, RequestMethod requestMethod) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.support(url, requestMethod))
                .findFirst()
                .orElseThrow(() -> new NoHandlerFoundException(
                        "[%s %s]에 매핑된 핸들러가 존재하지 않습니다.".formatted(requestMethod.name(), url)
                ));
    }

    public HandlerMapping getHandlerMapping(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        for (HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.support(requestURI, requestMethod)) {
                return handlerMapping;
            }
        }
        throw new NoHandlerFoundException(
                "[%s %s]에 매핑된 핸들러가 존재하지 않습니다.".formatted(requestMethod.name(), requestURI)
        );
    }

    protected List<HandlerMapping> getHandlerMappings() {
        return Collections.unmodifiableList(handlerMappings);
    }
}
