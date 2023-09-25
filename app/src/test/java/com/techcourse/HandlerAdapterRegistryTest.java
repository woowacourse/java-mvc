package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappingRegistry;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerAdapterRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        final List<HandlerMapping> handlerMappings = List.of(
                new AnnotationHandlerMapping("com.techcourse")
        );
        handlerMappingRegistry = new HandlerMappingRegistry(handlerMappings);
        final List<HandlerAdapter> handlerAdapters = List.of(
                new HandlerExecutionHandlerAdapter()
        );
        handlerAdapterRegistry = new HandlerAdapterRegistry(handlerAdapters);
    }

    @Test
    void register_post_요청에_대한_handlerAdapter를_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");

        final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);
        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(request, handlerMapping);

        assertThat(handlerAdapter.getClass()).isEqualTo(HandlerExecutionHandlerAdapter.class);
    }

    @Test
    void register_get_요청에_대한_handlerAdapter를_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);
        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(request, handlerMapping);

        assertThat(handlerAdapter.getClass()).isEqualTo(HandlerExecutionHandlerAdapter.class);
    }
}
