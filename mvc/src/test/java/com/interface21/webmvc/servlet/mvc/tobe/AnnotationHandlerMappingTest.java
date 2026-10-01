package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.web.bind.annotation.RequestMethod;
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
    @DisplayName("동일한 URL은 HTTP 메서드에 따라 서로 다른 컨트롤러 메서드로 매핑된다")
    void sameUrlIsMappedByHttpMethod() throws Exception {
        // given
        final var getRequest = mock(HttpServletRequest.class);
        when(getRequest.getRequestURI()).thenReturn("/same-test");
        when(getRequest.getMethod()).thenReturn("GET");

        final var postRequest = mock(HttpServletRequest.class);
        when(postRequest.getRequestURI()).thenReturn("/same-test");
        when(postRequest.getMethod()).thenReturn("POST");

        final var response = mock(HttpServletResponse.class);

        // when
        final var getHandler = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(postRequest);
        final var getModelAndView = getHandler.handle(getRequest, response);
        final var postModelAndView = postHandler.handle(postRequest, response);

        // then
        assertThat(getModelAndView.getObject("method")).isEqualTo("GET");
        assertThat(postModelAndView.getObject("method")).isEqualTo("POST");
    }

    @Test
    @DisplayName("HTTP 메서드를 지정하지 않으면 모든 지원 메서드에 매핑된다")
    void omittedMethodMatchesEverySupportedHttpMethod() {
        for (final RequestMethod method : RequestMethod.values()) {
            // given
            final var request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/any-method");
            when(request.getMethod()).thenReturn(method.name());

            // when
            final var handler = handlerMapping.getHandler(request);

            // then
            assertThat(handler).isInstanceOf(HandlerExecution.class);
        }
    }
}
