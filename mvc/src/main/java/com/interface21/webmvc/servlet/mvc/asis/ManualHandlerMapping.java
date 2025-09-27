package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.mvc.mapping.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private final Map<String, Controller> handlerMappings = new HashMap<>();

    public void initialize() {
        // 현재 사용하는 레거시 컨트롤러가 없으므로 비워둡니다.
        // 예: handlerMappings.put("/", new ForwardController());
        log.info("Initialized ManualHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        log.debug("Request URI : {}", request.getRequestURI());
        return handlerMappings.get(request.getRequestURI());
    }
}
