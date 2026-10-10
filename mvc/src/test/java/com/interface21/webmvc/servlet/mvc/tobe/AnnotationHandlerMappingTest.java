package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @DisplayName("@RequestMapping의 method를 생략하면 모든 HTTP 메서드로 매핑된다")
    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void mapsAllMethodsWhenMethodIsOmitted(final RequestMethod method) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/all-test");
        when(request.getMethod()).thenReturn(method.name());

        final var execution = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(execution).isNotNull();
        assertThat(execution.handle(request, response).getObject("method")).isEqualTo(method.name());
    }

    @DisplayName("등록되지 않은 URL이면 null을 반환한다")
    @Test
    void returnsNullForUnmappedUrl() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/missing");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @DisplayName("URL은 같아도 HTTP 메서드가 다르면 null을 반환한다")
    @Test
    void returnsNullForUnmappedMethod() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("POST");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @DisplayName("enum에 없는 HTTP 메서드로 요청하면 예외 대신 null을 반환한다")
    @Test
    void returnsNullForUnsupportedHttpMethod() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("FOO");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @DisplayName("같은 URL과 HTTP 메서드가 중복 등록되면 초기화 시점에 실패한다")
    @Test
    void failsOnDuplicateMapping() {
        final var duplicateMapping = new AnnotationHandlerMapping("duplicate");

        assertThatThrownBy(duplicateMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("/duplicate");
    }
}
