package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
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
    void GET_요청에_매핑된_메서드를_실행한다() throws Exception {
        final var request = createRequest();
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void POST_요청에_매핑된_메서드를_실행한다() throws Exception {
        final var request = createRequest();
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void HTTP_메서드를_생략하면_모든_메서드를_지원한다(final RequestMethod requestMethod) throws Exception {
        final var request = createRequest();
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/all-methods");
        when(request.getMethod()).thenReturn(requestMethod.name());

        final var modelAndView = handlerMapping.getHandler(request).handle(request, response);

        assertThat(modelAndView.getObject("request")).isSameAs(request);
        assertThat(modelAndView.getObject("response")).isSameAs(response);
    }

    @ParameterizedTest
    @EnumSource(value = RequestMethod.class, names = {"GET", "POST"})
    void 여러_HTTP_메서드를_같은_핸들러에_매핑한다(final RequestMethod requestMethod) throws Exception {
        final var request = createRequest();
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/multiple-methods");
        when(request.getMethod()).thenReturn(requestMethod.name());

        final var modelAndView = handlerMapping.getHandler(request).handle(request, response);

        assertThat(modelAndView.getObject("controller")).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = RequestMethod.class, names = {"GET", "POST"})
    void 같은_URL도_HTTP_메서드에_따라_다른_핸들러를_실행한다(final RequestMethod requestMethod) throws Exception {
        final var request = createRequest();
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/shared-url");
        when(request.getMethod()).thenReturn(requestMethod.name());

        final var modelAndView = handlerMapping.getHandler(request).handle(request, response);

        assertThat(modelAndView.getObject("method")).isEqualTo(requestMethod.name());
    }

    @Test
    void 같은_컨트롤러의_메서드들은_하나의_인스턴스를_사용한다() throws Exception {
        final var request = createRequest();
        final var response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/all-methods");
        final var first = handlerMapping.getHandler(request).handle(request, response);
        when(request.getRequestURI()).thenReturn("/multiple-methods");
        final var second = handlerMapping.getHandler(request).handle(request, response);

        assertThat(first.getObject("controller")).isSameAs(second.getObject("controller"));
    }

    @Test
    void 등록되지_않은_URL이면_null을_반환한다() {
        final var request = createRequest();
        when(request.getRequestURI()).thenReturn("/unknown");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @ParameterizedTest
    @EnumSource(value = RequestMethod.class, names = {"GET", "POST"}, mode = EnumSource.Mode.EXCLUDE)
    void 등록되지_않은_HTTP_메서드이면_null을_반환한다(final RequestMethod requestMethod) {
        final var request = createRequest();
        when(request.getRequestURI()).thenReturn("/multiple-methods");
        when(request.getMethod()).thenReturn(requestMethod.name());

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"mappingfixtures.samecontroller", "mappingfixtures.differentcontrollers"})
    void 같은_URL과_HTTP_메서드를_중복_등록하면_초기화에_실패한다(final String basePackage) {
        final var duplicateMapping = new AnnotationHandlerMapping(basePackage);

        assertThatIllegalStateException()
                .isThrownBy(duplicateMapping::initialize)
                .withMessageContaining("/users")
                .withMessageContaining("GET");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "/mvc", "/get-test"})
    void 컨텍스트_경로를_제외한_URL로_핸들러를_찾는다(final String contextPath) throws Exception {
        final var request = createRequest();
        final var response = mock(HttpServletResponse.class);
        when(request.getContextPath()).thenReturn(contextPath);
        when(request.getRequestURI()).thenReturn(contextPath + "/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("gugu");

        final var modelAndView = handlerMapping.getHandler(request).handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    private HttpServletRequest createRequest() {
        final var request = mock(HttpServletRequest.class);
        when(request.getContextPath()).thenReturn("");
        return request;
    }
}
