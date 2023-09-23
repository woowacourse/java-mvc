package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class HandlerAdapterRegistry {

    private final Set<HandlerAdapter> handlerAdapters = new LinkedHashSet<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public void adaptHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final var handlerAdapter = handlerAdapters.stream()
                .filter(found -> found.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("cannot find supporting handler adapter"));

        final var modelAndView = handlerAdapter.handle(request, response, handler);
        final var view = modelAndView.getView();
        final var model = modelAndView.getModel();
        view.render(model, request, response);
    }

}
