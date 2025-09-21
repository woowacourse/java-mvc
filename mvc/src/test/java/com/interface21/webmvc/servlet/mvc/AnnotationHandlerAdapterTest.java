package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter adapter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        adapter = new AnnotationHandlerAdapter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("HandlerExecution 타입을 지원한다.")
    @Test
    void supports_HandlerExecution() {
        // given
        final HandlerExecution handler = mock(HandlerExecution.class);

        // when
        final boolean supports = adapter.supports(handler);

        // then
        assertThat(supports).isTrue();
    }

    @DisplayName("HandlerExecution이 아닌 타입은 지원하지 않는다.")
    @Test
    void supports_NotHandlerExecution() {
        // given
        final Object handler = new Object();

        // when
        final boolean supports = adapter.supports(handler);

        // then
        assertThat(supports).isFalse();
    }

    @DisplayName("HandlerExecution을 실행하고 결과를 그대로 반환한다.")
    @Test
    void handle() throws Exception {
        // given
        final HandlerExecution handler = mock(HandlerExecution.class);
        final ModelAndView expected = mock(ModelAndView.class);
        when(handler.handle(request, response)).thenReturn(expected);

        // when
        final ModelAndView actual = adapter.handle(request, response, handler);

        // then
        assertThat(actual).isSameAs(expected);
    }
}
