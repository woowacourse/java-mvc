package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import support.ManualHandlerMapping;
import support.asis.Controller;

class HandlerMappingRegistryTest {
    HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
    }

    @DisplayName("요청에 맞는 Handler를 반환한다 - 어노테이션 기반")
    @Test
    void getHandler_annotation() {
        // given
        final var request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        //then
        assertThat(handler).isPresent();
        assertThat(handler.get()).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("요청에 맞는 Handler를 반환한다 - 메뉴얼 기반")
    @Test
    void getHandler_manual() {
        // given
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        //then
        assertThat(handler).isPresent();
        assertThat(handler.get()).isInstanceOf(Controller.class);
    }

    @DisplayName("요청에 맞는 Handler를 반환한다.")
    @Test
    void getHandler_not_exist() {
        // given
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/non");
        when(request.getMethod()).thenReturn("GET");

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        //then
        assertThat(handler).isEmpty();
    }
}
