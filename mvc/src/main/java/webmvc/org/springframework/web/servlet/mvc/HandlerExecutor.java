package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutor {

    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public HandlerExecutor(final HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    public void render(final HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        final var modelAndView = handlerAdapter.handle(request, response, handler);
        final var model = modelAndView.getModel();
        final var view = modelAndView.getView();

        view.render(model, request, response);
    }
}
