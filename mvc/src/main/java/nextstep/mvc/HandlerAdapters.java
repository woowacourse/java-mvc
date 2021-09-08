package nextstep.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters;

    public HandlerAdapters() {
        this(new ArrayList<>());
    }

    public HandlerAdapters(HandlerAdapter... adapters) {
        this(Arrays.asList(adapters));
    }

    public HandlerAdapters(List<HandlerAdapter> adapters) {
        this.adapters = new ArrayList<>(adapters);
    }

    public void add(HandlerAdapter handlerAdapter) {
        this.adapters.add(handlerAdapter);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return findHandlerAdapter(handler).handle(request, response, handler);
    }

    private HandlerAdapter findHandlerAdapter(Object handler) {
        return adapters.stream()
                       .filter(handlerAdapter -> handlerAdapter.supports(handler))
                       .findAny()
                       .orElseThrow(() -> new IllegalArgumentException("처리할 수 없는 핸들러입니다."));
    }
}
