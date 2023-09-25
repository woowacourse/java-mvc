package webmvc.org.springframework.web.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptorRegistry {
    private final List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public void addHandlerAdaptor(final HandlerAdaptor handlerAdaptor) {
        handlerAdaptors.add(handlerAdaptor);
    }

    public HandlerAdaptor getHandlerAdaptor(final Object handler) {
        return handlerAdaptors.stream()
                .filter(handlerAdaptor -> handlerAdaptor.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 핸들러를 처리할 수 있는 핸들러 어댑터가 없습니다."));
    }
}
