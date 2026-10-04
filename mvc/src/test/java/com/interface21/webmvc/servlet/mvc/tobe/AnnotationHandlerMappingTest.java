package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

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

    @DisplayName("@RequestMapping에 method를 지정하지 않으면 모든 HTTP 메서드를 지원한다.")
    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void allMethods(final RequestMethod requestMethod) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/all-method-test");
        when(request.getMethod()).thenReturn(requestMethod.name());

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("handler")).isEqualTo("allMethods");
    }

    @DisplayName("URL이 같아도 HTTP 메서드가 다르면 다른 핸들러가 실행된다.")
    @Test
    void sameUrlDifferentMethod() throws Exception {
        final var getRequest = mock(HttpServletRequest.class);
        final var postRequest = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(getRequest.getRequestURI()).thenReturn("/method-test");
        when(getRequest.getMethod()).thenReturn("GET");
        when(postRequest.getRequestURI()).thenReturn("/method-test");
        when(postRequest.getMethod()).thenReturn("POST");

        final var getHandler = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(postRequest);

        assertThat(getHandler.handle(getRequest, response).getObject("handler")).isEqualTo("get");
        assertThat(postHandler.handle(postRequest, response).getObject("handler")).isEqualTo("post");
    }

    @DisplayName("method에 여러 HTTP 메서드를 지정하면 그 메서드들로 모두 매핑된다.")
    @ParameterizedTest
    @ValueSource(strings = {"GET", "PUT"})
    void multipleMethods(final String requestMethod) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/multi-method-test");
        when(request.getMethod()).thenReturn(requestMethod);

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("handler")).isEqualTo("getOrPut");
    }

    @DisplayName("method에 지정하지 않은 HTTP 메서드로 요청하면 핸들러를 찾지 못한다.")
    @Test
    void notMappedMethod() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/multi-method-test");
        when(request.getMethod()).thenReturn("DELETE");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @DisplayName("매핑되지 않은 URL로 요청하면 핸들러를 찾지 못한다.")
    @Test
    void notMappedUrl() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/no-such-url");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
