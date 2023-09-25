package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters = new ArrayList<>();

    public void init() {
        adapters.add(new AnnotationHandlerAdapter());
    }

    public HandlerAdapter findAdapter(final Object handler) throws ServletException {
        return adapters.stream()
                .filter(adapter -> adapter.isSupport(handler))
                .findFirst()
                .orElseThrow(() -> new ServletException("No adapters support handler"));
    }

    public List<HandlerAdapter> getAdapters() {
        return adapters;
    }
}
