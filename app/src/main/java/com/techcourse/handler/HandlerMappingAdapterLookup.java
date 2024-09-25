package com.techcourse.handler;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.techcourse.HandlerMappingAdapter;

public class HandlerMappingAdapterLookup {

    public final List<HandlerMappingAdapter> lookups;

    public HandlerMappingAdapterLookup() {
        this.lookups = new ArrayList<>();
    }

    public int addAdapter(final HandlerMappingAdapter handlerMappingAdapter) {
        lookups.add(handlerMappingAdapter);
        return lookups.size();
    }

    public HandlerMappingAdapter get(final HttpServletRequest request) {
        return lookups.stream()
                .filter(handlerMappingAdapter -> handlerMappingAdapter.support(request))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("핸들러를 찾을 수 없는 HTTP request 요청"));
    }
}
