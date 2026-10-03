package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @DisplayName("GET /get-test 요청은 매핑된 메서드를 실행하고 id 모델을 반환한다")
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

    @DisplayName("POST /post-test 요청은 매핑된 메서드를 실행하고 id 모델을 반환한다")
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

    @DisplayName("HTTP 메서드를 지정하지 않은 매핑은 모든 RequestMethod 요청을 처리한다")
    @Test
    void allRequestMethodsWhenMethodIsNotSpecified() throws Exception {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);

            when(request.getAttribute("id")).thenReturn("gugu");
            when(request.getRequestURI()).thenReturn("/all-methods-test");
            when(request.getMethod()).thenReturn(requestMethod.name());

            final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
            assertThat(handlerExecution).as("HTTP method %s", requestMethod).isNotNull();

            final var modelAndView = handlerExecution.handle(request, response);
            assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        }
    }

    @DisplayName("같은 URL과 HTTP 메서드가 중복되면 초기화 중 예외가 발생한다")
    @Test
    void rejectsDuplicateMappingsAtInitialization() {
        final var duplicateMapping = new AnnotationHandlerMapping("duplicates");

        assertThatThrownBy(duplicateMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("/duplicate")
                .hasMessageContaining("GET");
    }

    @DisplayName("ModelAndView를 반환하지 않는 매핑 메서드는 초기화 중 예외가 발생한다")
    @Test
    void rejectsNonModelAndViewReturnTypeAtInitialization() {
        final var invalidMapping = new AnnotationHandlerMapping("invalidreturns");

        assertThatThrownBy(invalidMapping::initialize)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("알 수 없는 HTTP 메서드는 매핑되지 않는다")
    @Test
    void unknownHttpMethodHasNoHandler() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("BREW");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
