package com.interface21.webmvc.servlet.mvc.tobe.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JsonView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("extra", "samples");
        handlerMapping.initialize();
    }

    @Test
    void get_jspView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertAll(
                () -> assertThat(modelAndView.getView()).isInstanceOf(JspView.class),
                () -> assertThat(modelAndView.getObject("id")).isEqualTo("gugu")
        );
    }

    @Test
    void post_jspView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertAll(
                () -> assertThat(modelAndView.getView()).isInstanceOf(JspView.class),
                () -> assertThat(modelAndView.getObject("id")).isEqualTo("gugu")
        );
    }

    @Test
    void api_jsonView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/api/account-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("jojo");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertAll(
                () -> assertThat(modelAndView.getView()).isInstanceOf(JsonView.class),
                () -> assertThat(modelAndView.getObject("account")).isEqualTo("jojo")
        );
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void method_설정이_없으면_모든_HTTP_method를_지원한다(RequestMethod requestMethod) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/all-method-test");
        when(request.getMethod()).thenReturn(requestMethod.name());

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertAll(
                () -> assertThat(modelAndView.getView()).isInstanceOf(JspView.class),
                () -> assertThat(modelAndView.getObject("id")).isEqualTo("gugu")
        );
    }
}
