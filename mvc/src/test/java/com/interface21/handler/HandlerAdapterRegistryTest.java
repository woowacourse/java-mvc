package com.interface21.handler;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("핸들러 매핑 어텁터 목록을 추가한다.")
    void add_handler_mapping_adapter_list() {
        // given
        final HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        final AnnotationHandlerAdapter handlerMappingAdapter = new AnnotationHandlerAdapter("support");

        // when
        final int size = registry.addAdapter(handlerMappingAdapter);

        // then
        assertThat(size).isEqualTo(1);
    }

    @Test
    @DisplayName("Http request로 핸들러 매핑 어텁터를 가져온다.")
    void get_handler_mapping_adapter_via_http_request() {
        // given
        final HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        final AnnotationHandlerAdapter expect = new AnnotationHandlerAdapter("samples");
        registry.addAdapter(expect);
        final HttpServletRequest request = new MockHttpServletRequest("GET", "/get");

        // when
        final HandlerAdapter actual = registry.get(request);

        // then
        assertThat(actual).isEqualTo(expect);
    }
}
