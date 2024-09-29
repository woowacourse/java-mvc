package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.handler.AnnotationHandlerMapping;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("@RequestMapping 애노테이션에 Method가 따로 명시되어 있지 않다면 모든 메서드를 지원할 수 있는 핸들러로 간주하여 처리한다.")
    @EnumSource(value = RequestMethod.class)
    @ParameterizedTest
    void noMethodRequestMapping(final RequestMethod requestMethod) throws Exception {
        // Given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("kelly");
        when(request.getRequestURI()).thenReturn("/no-method-test");
        when(request.getMethod()).thenReturn(requestMethod.name());

        // When
        final var handlerExecution = handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        // Then
        assertThat(modelAndView.getObject("id")).isEqualTo("kelly");
    }

    @DisplayName("null 혹은 공백의 패키지 명이 입력되면 예외를 발생시킨다.")
    @EmptySource
    @ParameterizedTest
    void validateBasePackageIsNullOrBlank(final String input) {
        // When & Then
        assertThatThrownBy(() -> new AnnotationHandlerMapping(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("패키지 명은 null 혹은 공백일 수 없습니다. - " + input);
    }
}
