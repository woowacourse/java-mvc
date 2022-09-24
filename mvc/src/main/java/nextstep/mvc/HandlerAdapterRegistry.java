package nextstep.mvc;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerAdapterRegistry {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerAdapter> handlerAdapters;

    public void add(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> {
                    log.info("not found handlerAdapter by {}", handler);
                    throw new RuntimeException("handlerAdapter 가 존재하지 않습니다.");
                });
    }
}
