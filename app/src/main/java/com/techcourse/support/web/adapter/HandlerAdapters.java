package com.techcourse.support.web.adapter;

import com.techcourse.support.web.resolver.ViewResolvers;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapter = new ArrayList<>();

    public void initialize(final ViewResolvers viewResolvers) {
        final HandlerAdapter manualHandlerMappingAdapter = new ManualHandlerMappingAdapter(viewResolvers);
        final HandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        adapter.add(manualHandlerMappingAdapter);
        adapter.add(annotationHandlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter adapter : adapter) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        throw new IllegalStateException("해당 Handler를 수행할 수 있는 HandlerAdapter가 존재하지 않습니다.");
    }
}
