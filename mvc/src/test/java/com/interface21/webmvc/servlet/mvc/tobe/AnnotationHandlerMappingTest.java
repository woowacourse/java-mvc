package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        final var mapping = new AnnotationHandlerMapping("samples");
        mapping.initialize();
        handlerMapping = mapping;
    }

    @Test
    @DisplayName("GET 요청에 매핑된 핸들러를 실행하여 모델을 반환한다")
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
    @DisplayName("POST 요청에 매핑된 핸들러를 실행하여 모델을 반환한다")
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
    @DisplayName("HTTP 메서드를 지정하지 않으면 모든 요청 메서드를 지원한다")
    void supportsAllHttpMethodsWhenRequestMethodIsNotSpecified() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/all-test");

        for (RequestMethod requestMethod : RequestMethod.values()) {
            when(request.getMethod()).thenReturn(requestMethod.name());

            assertThat(handlerMapping.getHandler(request)).isNotNull();
        }
    }

    @Test
    @DisplayName("URL과 HTTP 메서드가 중복되면 초기화 중 예외를 던진다")
    void throwsExceptionWhenHandlerMappingIsDuplicated() {
        final var duplicatedHandlerMapping = new AnnotationHandlerMapping("fixtures.duplicate");

        assertThatThrownBy(duplicatedHandlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate handler mapping")
                .hasMessageContaining("/duplicate")
                .hasMessageContaining("GET");
    }

    @Test
    @DisplayName("등록되지 않은 요청 URI는 null을 반환한다")
    void returnsNullForUnmappedRequest() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unmapped");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    @DisplayName("URL이 같아도 HTTP 메서드가 다르면 null을 반환한다")
    void returnsNullWhenHttpMethodDoesNotMatch() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("POST");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
