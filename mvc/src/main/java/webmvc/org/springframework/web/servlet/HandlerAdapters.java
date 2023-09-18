package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {
    private final List<HandlerAdapter> adapter = new ArrayList<>();

    public void add(final HandlerAdapter handlerAdapter) {
        adapter.add(handlerAdapter);
    }

    public HandlerAdapter getAdaptor(final Object request) {
        return adapter.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(request))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
