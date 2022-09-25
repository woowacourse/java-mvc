package nextstep.mvc.handleradapter;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void add(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public ModelAndView getModelAndView(final Object handler, final HttpServletRequest request, final HttpServletResponse response) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.supports(handler))
            .map(handlerAdapter -> getModelAndView(handler, request, response, handlerAdapter))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("적절한 handler 가 존재하지 않습니다. [%s]", handler)));
    }

    private ModelAndView getModelAndView(final Object handler,
        final HttpServletRequest request, final HttpServletResponse response, final HandlerAdapter handlerAdapter
    ){
        try {
            return handlerAdapter.handle(request, response, handler);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("적절하지 않은 handler 입니다. [%s]", handler));
        }
    }
}
