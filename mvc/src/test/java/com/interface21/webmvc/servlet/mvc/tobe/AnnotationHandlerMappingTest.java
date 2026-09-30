package com.interface21.webmvc.servlet.mvc.tobe;

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
    @DisplayName("부모 클래스에 선언된 @RequestMapping 메서드도 자식 컨트롤러 인스턴스로 실행된다")
    void inheritedMethod() throws Exception {
        final var handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        final var request = mockRequest("/base-test", "GET");
        final var response = mock(HttpServletResponse.class);

        final var handler = (HandlerExecution) handlerMapping.getHandler(request);
        assertThat(handler).isNotNull();

        final var modelAndView = handler.handle(request, response);
        assertThat(modelAndView.getModel().get("owner")).isEqualTo("ChildController");
    }

    @Test
    @DisplayName("HTTP 메서드를 지정하지 않으면 모든 메서드 요청을 처리한다")
    void allMethodsWhenNotSpecified() {
        final var handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        for (final String method : new String[]{"GET", "POST", "PUT", "DELETE"}) {
            assertThat(handlerMapping.getHandler(mockRequest("/any-method-test", method)))
                    .as(method)
                    .isNotNull();
        }
    }

    @Test
    @DisplayName("매핑되지 않은 URL은 null을 반환한다")
    void unknownUrl() {
        final var handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        assertThat(handlerMapping.getHandler(mockRequest("/unknown", "GET"))).isNull();
    }

    @Test
    @DisplayName("같은 URL과 HTTP 메서드가 중복 등록되면 예외가 발생한다")
    void duplicateMapping() {
        final var handlerMapping = new AnnotationHandlerMapping("duplicate");

        assertThatThrownBy(handlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class);
    }

    private HttpServletRequest mockRequest(final String uri, final String method) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
        return request;
    }
}
