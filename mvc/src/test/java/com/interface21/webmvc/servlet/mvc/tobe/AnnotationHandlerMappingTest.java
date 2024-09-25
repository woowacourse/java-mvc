package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.interface21.web.bind.annotation.RequestMethod;

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

    @DisplayName("request method가 없으면 모든 메서드를 등록한다.")
    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void allRequestMethod(RequestMethod method) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("method")).thenReturn(method);
        when(request.getRequestURI()).thenReturn("/method-test");
        when(request.getMethod()).thenReturn(method.name());

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("method")).isEqualTo(method);
    }

    @DisplayName("RequestMapping의 value가 없으면 '/' 를 기본값으로 설정한다.")
    @Test
    void defaultUri() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("emptyUriTest")).isEqualTo("success");
    }

    @DisplayName("요청에 맞는 핸들러가 존재하는지 확인한다.")
    @Test
    void hasHandler() {
        final var trueRequest = mock(HttpServletRequest.class);
        when(trueRequest.getRequestURI()).thenReturn("/get-test");
        when(trueRequest.getMethod()).thenReturn("GET");

        final var falseRequest = mock(HttpServletRequest.class);
        when(falseRequest.getRequestURI()).thenReturn("/none");
        when(falseRequest.getMethod()).thenReturn("GET");

        assertAll(
                () -> assertThat(handlerMapping.hasHandler(trueRequest)).isTrue(),
                () -> assertThat(handlerMapping.hasHandler(falseRequest)).isFalse()
        );
    }

    @DisplayName("중복된 핸들러가 존재하면 예외가 발생한다.")
    @Test
    void tes() {
        assertThatThrownBy(handlerMapping::initialize)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
