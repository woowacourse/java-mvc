package webmvc.org.springframework.web.servlet.mvc.supports.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.supports.HandlerMapping;

public class HandlerMappings {

    private final List<HandlerMapping> mapping = new ArrayList<>();

    public void addHandlerMapping(final HandlerMapping targetHandlerMapping) {
        mapping.add(targetHandlerMapping);
    }

    public void initialize() {
        for (final HandlerMapping targetHandlerMapping : mapping) {
            targetHandlerMapping.initialize();
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        for (final HandlerMapping targetHandlerMapping : mapping) {
            final Object handler = targetHandlerMapping.getHandler(request);

            if (handler != null) {
                return handler;
            }
        }

        return null;
    }
}
