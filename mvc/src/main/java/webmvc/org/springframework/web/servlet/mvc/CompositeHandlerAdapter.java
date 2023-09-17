package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CompositeHandlerAdapter implements HandlerAdapter {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public CompositeHandlerAdapter(HandlerAdapter... args) {
        handlerAdapters.addAll(Arrays.asList(args));
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return getAdapter(handler)
                .map(adapter -> adapter.handle(request, response, handler))
                .orElse(null);
    }

    @Override
    public boolean supports(Object handler) {
        return getAdapter(handler).isPresent();
    }

    private Optional<HandlerAdapter> getAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny();
    }
}
