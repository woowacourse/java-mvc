package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecutor {

    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public HandlerExecutor() {
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdaptor) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdaptor);
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
        final Object handler) {
        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        return handlerAdapter.handle(request, response, handler);
    }
}
