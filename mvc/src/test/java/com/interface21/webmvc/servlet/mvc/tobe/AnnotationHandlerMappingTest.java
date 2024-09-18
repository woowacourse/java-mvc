package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import com.sun.net.httpserver.Request;

class AnnotationHandlerMappingTest {
    private static final String BASE_PACKAGE = "samples";
    private static final String REQUEST_URI_GET_TEST = "/get-test";
    private static final String REQUEST_URI_WRONG = "/get-example";
    private static final String REQUEST_URI_POST_TEST = "/post-test";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn(REQUEST_URI_GET_TEST);
        when(request.getMethod()).thenReturn(METHOD_GET);

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn(REQUEST_URI_POST_TEST);
        when(request.getMethod()).thenReturn(METHOD_POST);

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("HttpServletRequest로 적절한 HandlerExecution을 반환한다.")
    @Test
    void getHandlerTest() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_GET_TEST);
        when(request.getMethod()).thenReturn(METHOD_GET);

        final HandlerKey handlerKey = new HandlerKey(REQUEST_URI_GET_TEST, RequestMethod.of(METHOD_GET));
        final HandlerExecution handlerExecution = new HandlerExecution(null, null);
        handlerMapping.addHandler(handlerKey, handlerExecution);

        // when
        final HandlerExecution actual = (HandlerExecution) handlerMapping.getHandler(request);

        // then
        final HandlerExecution expected = handlerExecution;

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("HttpServletRequest로 HandlerExecution을 찾지 못하면 IllegalArgumentException을 던진다.")
    @Test
    void getHandlerTestThrowExceptionWhenNotFound() {
        // given
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn(REQUEST_URI_WRONG);
        when(request.getMethod()).thenReturn(METHOD_GET);

        // when & then
        assertThatThrownBy(() -> handlerMapping.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("적절하지 않은 요청입니다.");
    }
}
