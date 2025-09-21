package com.interface21.webmvc.servlet.mapping;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotationHandlerMapping implements HandlerMapping {

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public static AnnotationHandlerMapping from(final Object... basePackages) {
        final Map<HandlerKey, HandlerExecution> handlerExecutions = new ControllerScanner(basePackages).process();
        return new AnnotationHandlerMapping(handlerExecutions);
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));

        return handlerExecutions.get(handlerKey);
    }
}
