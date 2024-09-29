package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

class HandlerMappingRegistryTest {

    @Nested
    @DisplayName("요청에 맞는 HandlerMapping 반환")
    class GetHandlerMapping {

        @Test
        @DisplayName("요청에 맞는 Handle 반환 성공: HandlerExecution")
        void getHandlerMapping_AnnotationHandlerMapping() {
            final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry("samples");
            final HttpServletRequest request = mock(HttpServletRequest.class);

            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestURI()).thenReturn("/get-test");

            final Optional<Object> handler = handlerMappingRegistry.getHandler(request);

            assertThat(handler).containsInstanceOf(HandlerExecution.class);
        }

        @Test
        @DisplayName("요청에 맞는 HandlerMapping 반환 실패: 매칭되는 HandlerMapping이 없는 경우")
        void getHandlerMapping_IllegalArgumentException() {
            final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry("com.example");
            final HttpServletRequest request = mock(HttpServletRequest.class);

            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestURI()).thenReturn("/nonexistent");

            assertThat(handlerMappingRegistry.getHandler(request)).isEmpty();
        }
    }
}
