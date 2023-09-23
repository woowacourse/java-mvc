package webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerAdapterNotExistException;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters = new ArrayList<>();

    public void addAdapter(final HandlerAdapter handlerAdapter) {
        adapters.add(handlerAdapter);
    }

    public ModelAndView handle(final Object handler, final HttpServletRequest request,
        final HttpServletResponse response) throws Exception {
        for (HandlerAdapter adapter : adapters) {
            if (adapter.isHandleable(handler)) {
                return adapter.handle(handler, request, response);
            }
        }
        throw new HandlerAdapterNotExistException();
    }
}
