package com.techcourse.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.techcourse.HandlerMappingAdapter;

import support.FakeHttpServletRequest;

class HandlerMappingAdapterLookupTest {

    @Test
    @DisplayName("핸들러 매핑 어텁터 목록을 추가한다.")
    void add_handler_mapping_adapter_list() {
        // given
        final HandlerMappingAdapterLookup lookup = new HandlerMappingAdapterLookup();
        final AnnotationHandlerMappingAdapter handlerMappingAdapter = new AnnotationHandlerMappingAdapter("support");

        // when
        final int size = lookup.addAdapter(handlerMappingAdapter);

        // then
        assertThat(size).isEqualTo(1);
    }

    @Test
    @DisplayName("Http request로 핸들러 매핑 어텁터를 가져온다.")
    void get_handler_mapping_adapter_via_http_request() {
        // given
        final HandlerMappingAdapterLookup lookup = new HandlerMappingAdapterLookup();
        final AnnotationHandlerMappingAdapter expect = new AnnotationHandlerMappingAdapter("support");
        lookup.addAdapter(expect);
        final FakeHttpServletRequest request = new FakeHttpServletRequest("GET", "/get");

        // when
        final HandlerMappingAdapter actual = lookup.get(request);

        // then
        assertThat(actual).isEqualTo(expect);
    }
}
