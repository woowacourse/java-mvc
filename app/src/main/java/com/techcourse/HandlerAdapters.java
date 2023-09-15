package com.techcourse;

import com.techcourse.adapter.ManualHandlerMappingAdapter;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters = new ArrayList<>();

    public void initialize(final ViewResolvers viewResolvers) {
        final ManualHandlerMappingAdapter manualHandlerMappingAdapter = new ManualHandlerMappingAdapter(viewResolvers);

        adapters.add(manualHandlerMappingAdapter);
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
