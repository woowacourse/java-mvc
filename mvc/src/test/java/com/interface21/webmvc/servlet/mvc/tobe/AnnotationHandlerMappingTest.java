package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
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
    void supportsAllMethodsWhenMethodIsUnspecified(final RequestMethod method) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/all-methods-test");
        when(request.getMethod()).thenReturn(method.name());

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        assertThat(handlerExecution).isNotNull();
        assertThat(handlerExecution.handle(request, response).getObject("id")).isEqualTo("gugu");
    }

    @Test
    void throwExceptionWhenDuplicatedMethodAndUrl() {
        var mapping = new AnnotationHandlerMapping("wrongsamples.duplicate");

        assertThatThrownBy(() -> mapping.initialize())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복된 요청 매핑입니다.");
    }

    @Test
    void throwExceptionWhenInvalidParameter() {
        var mapping = new AnnotationHandlerMapping("wrongsamples.invalidparameter");

        assertThatThrownBy(() -> mapping.initialize())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("잘못된 컨트롤러 매개변수 입니다.:");
    }

    @Test
    void throwExceptionWhenInvalidReturnType() {
        var mapping = new AnnotationHandlerMapping("wrongsamples.invalidreturntype");

        assertThatThrownBy(() -> mapping.initialize())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("잘못된 컨트롤러 반환타입 입니다.:");
    }
}
