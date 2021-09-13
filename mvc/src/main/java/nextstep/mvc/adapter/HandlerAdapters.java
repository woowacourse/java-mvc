package nextstep.mvc.adapter;

import nextstep.mvc.handler.controller.ControllerBasedAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdapters.class);

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void init() {
        log.info("Adding default handler adapters");
        addDefaultHandlerAdapters();
    }

    private void addDefaultHandlerAdapters() {
        handlerAdapters.add(new AnnotationBasedAdapter());
        log.info("added AnnotationBasedAdapter as default");
        handlerAdapters.add(new ControllerBasedAdapter());
        log.info("added ControllerBasedAdapter as default");
    }

    public HandlerAdapter getAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new AdapterNotFoundException(handler));
    }
}
