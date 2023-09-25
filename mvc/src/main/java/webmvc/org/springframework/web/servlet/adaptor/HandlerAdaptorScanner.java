package webmvc.org.springframework.web.servlet.adaptor;

import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptorScanner {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdaptorScanner.class);

    private final List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public HandlerAdaptorScanner() {
        handlerAdaptors.add(new ManualHandlerAdaptor());
        handlerAdaptors.add(new AnnotationHandlerAdaptor());
    }

    public HandlerAdaptor getHandlerAdaptor(final Object handler) throws ServletException {
        for (final HandlerAdaptor handlerAdaptor : handlerAdaptors) {
            if (handlerAdaptor.supports(handler)) {
                return handlerAdaptor;
            }
        }

        log.error("해당 handler 를 지원하는 어댑터가 없습니다. handler 정보 = {}", handler.getClass());

        throw new ServletException("핸들러를 지원하는 어댑터가 없습니다.");
    }
}
