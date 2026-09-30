package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
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

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void 등록되지_않은_URL이면_null을_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/not-registered");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 등록된_URL이지만_지원하지_않는_HTTP_메서드면_null을_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("DELETE");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 존재하지_않는_HTTP_메서드면_null을_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("FOO");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 핸들러_메서드의_반환_타입이_ModelAndView가_아니면_초기화할_때_예외가_발생한다() {
        final var invalidHandlerMapping = new AnnotationHandlerMapping("invalidsamples.returntype");

        assertThatThrownBy(invalidHandlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 핸들러_메서드의_파라미터가_요청과_응답이_아니면_초기화할_때_예외가_발생한다() {
        final var invalidHandlerMapping = new AnnotationHandlerMapping("invalidsamples.parameter");

        assertThatThrownBy(invalidHandlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class);
    }
}
