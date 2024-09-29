package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ManualController;

class HandlerMappingRegistryTest {

    private static final String BASE_PACKAGE = "samples";

    @Test
    @DisplayName("서로 다른 핸들러 매핑이 존재해도 메뉴얼 핸들러를 찾을 수 있다.")
    void getManualHandler() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMappingRegistry registry = new HandlerMappingRegistry(BASE_PACKAGE);
        registry.addHandlerMapping(new ManualHandlerMapping());
        registry.initialize();

        // when
        when(request.getRequestURI()).thenReturn("/manual");
        when(request.getMethod()).thenReturn("GET");
        Optional<Object> handler = registry.getHandler(request);

        // then
        assertThat(handler).isPresent();
        assertThat(handler.get()).isInstanceOf(ManualController.class);
    }

    @Test
    @DisplayName("서로 다른 핸들러 매핑이 존재해도 어노테이션 핸들러를 찾을 수 있다.")
    void getAnnotationHandler() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HandlerMappingRegistry registry = new HandlerMappingRegistry(BASE_PACKAGE);
        registry.addHandlerMapping(new ManualHandlerMapping());
        registry.initialize();

        // when
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");
        Optional<Object> handler = registry.getHandler(request);

        // then
        assertThat(handler).isPresent();
        assertThat(handler.get()).isInstanceOf(HandlerExecution.class);
    }
}