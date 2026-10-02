package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("같은 URL이라도 HTTP 메서드가 다르면 핸들러를 찾지 못한다")
    void doesNotFindHandlerWhenHttpMethodDiffers() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("POST");

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isNull();
    }

    @Test
    @DisplayName("HTTP 메서드를 지정하지 않으면 모든 HTTP 메서드에서 핸들러를 찾는다")
    void findsHandlerForEveryHttpMethodWhenMethodIsOmitted() {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            // given
            final var request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/all-methods");
            when(request.getMethod()).thenReturn(requestMethod.name());

            // when
            final var handler = handlerMapping.getHandler(request);

            // then
            assertThat(handler).as(requestMethod.name()).isNotNull();
        }
    }

    @Test
    @DisplayName("같은 URL이라도 HTTP 메서드에 따라 다른 핸들러를 실행한다")
    void invokesDifferentHandlersForSameUrl() throws Exception {
        // given
        final var getRequest = mock(HttpServletRequest.class);
        final var postRequest = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(getRequest.getRequestURI()).thenReturn("/same-url");
        when(getRequest.getMethod()).thenReturn("GET");
        when(postRequest.getRequestURI()).thenReturn("/same-url");
        when(postRequest.getMethod()).thenReturn("POST");

        // when
        final var getHandler = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(postRequest);

        // then
        assertThat(getHandler.handle(getRequest, response).getObject("handler")).isEqualTo("GET");
        assertThat(postHandler.handle(postRequest, response).getObject("handler")).isEqualTo("POST");
    }
}
