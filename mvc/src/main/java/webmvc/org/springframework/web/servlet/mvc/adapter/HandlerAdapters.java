package webmvc.org.springframework.web.servlet.mvc.adapter;

import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.view.resolver.ViewResolvers;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters = new ArrayList<>();

    public void initialize(final ViewResolvers viewResolvers) {
        final HandlerAdapter manualHandlerMappingAdapter = new ManualHandlerMappingAdapter(viewResolvers);
        final HandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        adapters.add(manualHandlerMappingAdapter);
        adapters.add(annotationHandlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter adapter : adapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        throw new IllegalStateException("해당 Handler를 수행할 수 있는 HandlerAdapter가 존재하지 않습니다.");
    }
}
