package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdopters {

    private final List<HandlerAdopter> handlerAdopters;

    public HandlerAdopters(final List<HandlerAdopter> handlerAdopters) {
        this.handlerAdopters = handlerAdopters;
    }

    public void add(final HandlerAdopter adopter) {
        handlerAdopters.add(adopter);
    }

    public HandlerAdopter getAdopter(final Object handler) {
        return handlerAdopters.stream()
                .filter(adopter -> adopter.isSupport(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find suitable adopter))"));
    }
}
