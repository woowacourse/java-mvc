package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.HandlerMapper;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.HandlerMappers;

public class HandlerRegistry {

    private final HandlerMappers handlerMappers;
    private final HandlerAdapters handlerAdapters;

    public HandlerRegistry() {
        this.handlerMappers = new HandlerMappers();
        this.handlerAdapters = new HandlerAdapters();
    }

    public void addHandler(final HandlerMapper mapper, final HandlerAdapter adapter) {
        handlerMappers.addHandlerMapper(mapper);
        handlerAdapters.addHandlerAdapter(adapter);
    }

    public void initHandlers() {
        handlerMappers.init();
    }

    public Object findHandlerMapper(final HttpServletRequest request) {
        return handlerMappers.findHandlerMapper(request);
    }

    public HandlerAdapter findHandlerAdapter(final Object handler) {
        return handlerAdapters.findHandlerAdapter(handler);
    }
}
