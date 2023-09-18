package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters = new ArrayList<>();

    public void init() {
        adapters.add(new ManualHandlerAdapter());
        adapters.add(new AnnotationHandlerAdapter());
    }

    public HandlerAdapter findAdapter(final Object handler) {
        return adapters.stream()
                .filter(adapter -> adapter.isSupport(handler))
                .findFirst()
                .orElse(null);
    }

}
