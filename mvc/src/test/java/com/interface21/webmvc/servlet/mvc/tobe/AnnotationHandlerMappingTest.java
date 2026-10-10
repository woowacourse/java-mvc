package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

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
        when(request.getContextPath()).thenReturn("");
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
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void context_path를_제외한_경로로_핸들러를_찾는다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/app/get-test");
        when(request.getContextPath()).thenReturn("/app");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void method를_생략하면_모든_HTTP_메서드에_매핑된다(final RequestMethod requestMethod) {
        final var request = mockRequest("/all-method-test", requestMethod.name());

        assertThat(handlerMapping.getHandler(request)).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST"})
    void 지정한_여러_HTTP_메서드에_매핑된다(final String method) {
        final var request = mockRequest("/multi-method-test", method);

        assertThat(handlerMapping.getHandler(request)).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({"GET, sameUrlGet", "POST, sameUrlPost"})
    void 같은_URL이라도_HTTP_메서드별로_다른_핸들러에_매핑된다(final String method, final String expectedHandler) throws Exception {
        final var request = mockRequest("/same-url-test", method);
        final var response = mock(HttpServletResponse.class);

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("handler")).isEqualTo(expectedHandler);
    }

    @ParameterizedTest
    @CsvSource({"/get-test, POST", "/multi-method-test, PUT"})
    void 지정하지_않은_HTTP_메서드로_요청하면_핸들러를_찾지_못한다(final String uri, final String method) {
        final var request = mockRequest(uri, method);

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 같은_URL과_HTTP_메서드의_핸들러가_중복되면_초기화에_실패한다() {
        final var duplicateMapping = new AnnotationHandlerMapping("duplicate");

        assertThatThrownBy(duplicateMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("GET /users")
                .hasMessageContaining("FirstDuplicateController#findUsers")
                .hasMessageContaining("SecondDuplicateController#findUsers");
    }

    private HttpServletRequest mockRequest(final String uri, final String method) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn(method);
        return request;
    }
}
