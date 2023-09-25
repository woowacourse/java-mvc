package webmvc.org.springframework.web.servlet;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdaptor;

public class HandlerAdapters {
    private final List<HandlerAdaptor> adapters;

    private HandlerAdapters(final List<HandlerAdaptor> adapters) {
        this.adapters = adapters;
    }

    public static HandlerAdapters create() {
        return new HandlerAdapters(initHandlerAdaptors());
    }

    private static List<HandlerAdaptor> initHandlerAdaptors() {
        return HandlerAdaptersFactory.createHandlerAdaptors()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public HandlerAdaptor getHandlerAdapter(final Object method) {
        return this.adapters.stream()
                .filter(handlerAdaptor -> handlerAdaptor.supports(method))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당 어댑터를 찾을 수 없습니다. input method: " + method));
    }
}
