package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;

class HandlerMappingRegistryTest {

    @Nested
    @DisplayName("요청에 맞는 HandlerMapping 반환")
    class GetHandlerMapping {

        @Test
        @DisplayName("요청에 맞는 HandlerMapping 반환 성공: AnnotationHandlerMapping")
        void getHandlerMapping_AnnotationHandlerMapping() {
            final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry("samples");
            final HttpServletRequest request = mock(HttpServletRequest.class);

            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestURI()).thenReturn("/get-test");

            final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);

            assertThat(handlerMapping).isInstanceOf(AnnotationHandlerMapping.class);
        }

        @Test
        @DisplayName("요청에 맞는 HandlerMapping 반환 실패: 매칭되는 HandlerMapping이 없는 경우")
        void getHandlerMapping_IllegalArgumentException() {
            final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry("com.example");
            final HttpServletRequest request = mock(HttpServletRequest.class);

            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestURI()).thenReturn("/nonexistent");

            assertThatThrownBy(() -> handlerMappingRegistry.getHandlerMapping(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 요청을 처리할 수 있는 핸들러가 없습니다.");
        }
    }
}
