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

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() throws Exception {
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
    void allMethods() throws Exception {
        final var getRequest = mock(HttpServletRequest.class);
        final var postRequest = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(getRequest.getRequestURI()).thenReturn("/all-methods-test");
        when(getRequest.getMethod()).thenReturn("GET");
        when(postRequest.getRequestURI()).thenReturn("/all-methods-test");
        when(postRequest.getMethod()).thenReturn("POST");

        final var getHandler = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(postRequest);

        assertThat(getHandler.handle(getRequest, response).getObject("mapping")).isEqualTo("all-methods");
        assertThat(postHandler.handle(postRequest, response).getObject("mapping")).isEqualTo("all-methods");
    }

    @Test
    void unsupportedMethod() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("CUSTOM");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void duplicateHandler() {
        final var duplicateHandlerMapping = new AnnotationHandlerMapping("mappingfixtures.duplicate");

        assertThatThrownBy(duplicateHandlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("/duplicate")
                .hasMessageContaining("GET");
    }

    @Test
    @DisplayName("어노테이션으로 등록한 요청을 HandlerExecution으로 핸들링한다")
    void handlesAnnotationRequest() {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/get-test");

        final Object handler = handlerMapping.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    @DisplayName("아무 핸들러 매핑도 처리할 수 없는 요청이면 null을 반환한다")
    void returnsEmptyWhenNoHandlerCanHandle() {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/unknown-get-uri");

        final Object handler = handlerMapping.getHandler(request);

        assertThat(handler).isNull();
    }
}
