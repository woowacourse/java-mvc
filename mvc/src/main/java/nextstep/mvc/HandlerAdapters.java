package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.NotFoundHandlerAdapterException;
import nextstep.mvc.view.ModelAndView;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void add(HandlerAdapter handlerAdaptor) {
        this.handlerAdapters.add(handlerAdaptor);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerAdapter handlerAdapter = findHandlerAdapter(handler);
        return handlerAdapter.handle(request, response, handler);
    }

    private HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new NotFoundHandlerAdapterException(handler));
    }
}
