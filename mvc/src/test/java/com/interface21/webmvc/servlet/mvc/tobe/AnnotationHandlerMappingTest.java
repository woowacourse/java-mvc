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
import org.junit.jupiter.params.provider.ValueSource;

class AnnotationHandlerMappingTest {
    private static final String BASE_PACKAGE = "samples";
    private static final String KEY = "id";
    private static final String VALUE = "id";
    private static final String REQUEST_URI_GET_TEST = "/get-test";
    private static final String REQUEST_URI_WRONG = "/get-example";
    private static final String REQUEST_URI_POST_TEST = "/post-test";
    private static final String REQUEST_URI_NO_REQUEST_METHOD = "/no-request-method-test";
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

        when(request.getAttribute(KEY)).thenReturn(VALUE);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_GET_TEST);
        when(request.getMethod()).thenReturn(METHOD_GET);

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject(KEY)).isEqualTo(VALUE);
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute(KEY)).thenReturn(VALUE);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_POST_TEST);
        when(request.getMethod()).thenReturn(METHOD_POST);

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject(KEY)).isEqualTo(VALUE);
    }

    @DisplayName("RequestMapping에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원한다.")
    @ValueSource(strings = {METHOD_GET, METHOD_POST})
    @ParameterizedTest
    void executeMethodWithNoRequestMethod(String requestMethod) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute(KEY)).thenReturn(VALUE);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_NO_REQUEST_METHOD);
        when(request.getMethod()).thenReturn(requestMethod);

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject(KEY)).isEqualTo(VALUE);
    }

    @DisplayName("HttpServletRequest로 HandlerExecution를 찾을 수 있다.")
    @Test
    void getHandlerTest() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_GET_TEST);
        when(request.getMethod()).thenReturn(METHOD_GET);

        // when
        final Object handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
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
