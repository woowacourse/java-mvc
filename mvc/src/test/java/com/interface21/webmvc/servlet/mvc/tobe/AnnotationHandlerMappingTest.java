package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void method를_지정하지_않으면_모든_HTTP_메서드를_매핑한다(final RequestMethod requestMethod) {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/all-test");
        when(request.getMethod()).thenReturn(requestMethod.name());

        assertThat(handlerMapping.getHandler(request)).isNotNull();
    }

    @Test
    void HTTP_메서드가_일치하지_않으면_null을_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("POST");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 지원하지_않는_HTTP_메서드면_예외_없이_null을_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/all-test");
        when(request.getMethod()).thenReturn("CONNECT");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
