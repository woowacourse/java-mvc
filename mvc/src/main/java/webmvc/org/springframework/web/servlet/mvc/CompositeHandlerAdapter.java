package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeHandlerAdapter implements HandlerAdapter {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public CompositeHandlerAdapter(HandlerAdapter... args) {
        handlerAdapters.addAll(Arrays.asList(args));
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .map(adapter -> adapter.handle(request, response, handler))
                .findAny()
                .orElseThrow(HandlerAdapterNotFoundException::new);
    }

    @Override
    public boolean supports(Object handler) {
        return handlerAdapters.stream()
                .anyMatch(adapter -> adapter.supports(handler));
    }
}
