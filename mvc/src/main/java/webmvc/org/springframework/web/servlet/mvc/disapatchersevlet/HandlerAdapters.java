package webmvc.org.springframework.web.servlet.mvc.disapatchersevlet;

import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void initialize() {
        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    public HandlerAdapter getAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupport(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("예기치 못한 에러"));
    }
}
