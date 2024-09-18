package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerMappings {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappings.class);

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(HandlerMapping... handlerMappings) {
        this.handlerMappings = Arrays.stream(handlerMappings).toList();
    }

    public Object findHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                return handlerMapping.getHandler(request);
            } catch (IllegalArgumentException e) {
                log.info("{} 에 존재하지 않는 요청 : {} {}", handlerMapping.getClass().getName(),
                        request.getMethod(), request.getRequestURI());
            }
        }
        throw new IllegalArgumentException("지원하는 handlerMapping 이 없습니다.");
    }
}
