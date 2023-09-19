package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.LinkedHashSet;
import java.util.Set;

public class HandlerAdapterRegistry {

    private final Set<HandlerAdapter> handlerAdapters = new LinkedHashSet<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public void adaptHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final var handlerSupport = handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst();

        if (handlerSupport.isPresent()) {
            final var handlerAdapter = handlerSupport.get();
            handlerAdapter.handle(request, response, handler);
        }
    }

}
