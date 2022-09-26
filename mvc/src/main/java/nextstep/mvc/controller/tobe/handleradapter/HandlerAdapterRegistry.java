package nextstep.mvc.controller.tobe.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.view.ModelAndView;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object object) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(object))
                .findAny()
                .orElseThrow();
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow();
        return handlerAdapter.handle(request, response, handler);
    }
}
