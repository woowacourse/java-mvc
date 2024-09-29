package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.mock.web.MockHttpServletRequest;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
    }

    @Test
    void GET_요청을_처리한다() throws Exception {
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
    void POST_요청을_처리한다() throws Exception {
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
    void 요청_메서드_타입이_지정되지_않았을_경우_모든_메서드를_지원한다(RequestMethod method) {
        // given
        HttpServletRequest request = new MockHttpServletRequest(method.name(), "/all-test");

        // when && then
        assertThatCode(() -> handlerMapping.getHandler(request)).doesNotThrowAnyException();
    }

    @Test
    void suppport() {
        // given
        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        HttpServletRequest request = new MockHttpServletRequest("GET", "/get-test");

        // when
        boolean actual = annotationHandlerMapping.support(request);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void getHandler() {
        // given
        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        HttpServletRequest request = new MockHttpServletRequest("GET", "/get-test");

        // when
        Object handler = annotationHandlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
