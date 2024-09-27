package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerMappingRegistry();
    }

    @Test
    @DisplayName("Request 에 맞는 적절한 HandlerMapping 을 찾아 Handler 를 반환한다.")
    void test() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        registry.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        Object handler = registry.getHandler(request);

        assertThat(handler.getClass()).isEqualTo(HandlerExecution.class);
    }

    @Test
    @DisplayName("Request 에 맞는 적절한 HandlerMapping 이 없는 경우 예외를 발생한다.")
    void test_exception() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("POST");

        registry.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        assertThatThrownBy(() -> registry.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
