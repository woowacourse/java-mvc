package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples", "camp.nextstep.controller");
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
    void 직접_선언한_public_메서드만_지원한다() {
        // given
        HttpServletRequest parentRequest = mock(HttpServletRequest.class);
        HttpServletRequest childRequest = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(parentRequest.getRequestURI()).thenReturn("/parent");
        when(parentRequest.getMethod()).thenReturn("GET");
        when(childRequest.getRequestURI()).thenReturn("/child");
        when(childRequest.getMethod()).thenReturn("GET");

        final HandlerExecution parentHandlerExecution =
            (HandlerExecution) handlerMapping.getHandler(parentRequest);
        final HandlerExecution childHandlerExecution =
            (HandlerExecution) handlerMapping.getHandler(childRequest);

        assertAll(
            () -> assertThat(parentHandlerExecution).isNull(),
            () -> assertThat(childHandlerExecution).isNotNull(),
            () -> assertThatCode(() ->
                childHandlerExecution.handle(childRequest, response))
                .doesNotThrowAnyException()
        );
    }
}
