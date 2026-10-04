package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
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

    @Test
    @DisplayName("동일한 URL 요청을 HTTP 메서드에 따라 서로 다른 핸들러로 매핑한다")
    void HTTP_메서드로_핸들러를_구분한다() throws Exception {
        final var getRequest = request("/method-test", RequestMethod.GET);
        final var postRequest = request("/method-test", RequestMethod.POST);
        final var response = mock(HttpServletResponse.class);

        final var getHandler = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(postRequest);

        assertThat(getHandler.handle(getRequest, response).getObject("handler"))
                .isEqualTo("get");
        assertThat(postHandler.handle(postRequest, response).getObject("handler"))
                .isEqualTo("post");
    }

    @Test
    @DisplayName("RequestMapping에 지정하지 않은 HTTP 메서드는 매핑하지 않는다")
    void 지정하지_않은_HTTP_메서드를_제외한다() {
        final var request = request("/method-test", RequestMethod.DELETE);

        final var handler = handlerMapping.getHandler(request);

        assertThat(handler).isNull();
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    @DisplayName("RequestMapping의 method를 생략하면 모든 HTTP 메서드를 지원한다")
    void 모든_HTTP_메서드를_지원한다(final RequestMethod requestMethod) throws Exception {
        final var request = request("/all-methods", requestMethod);
        final var response = mock(HttpServletResponse.class);

        final var handler = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(handler).isNotNull();
        assertThat(handler.handle(request, response).getObject("handler"))
                .isEqualTo("all");
    }

    @ParameterizedTest
    @EnumSource(value = RequestMethod.class, names = {"GET", "POST"})
    @DisplayName("RequestMapping에 지정한 여러 HTTP 메서드를 하나의 핸들러로 매핑한다")
    void 여러_HTTP_메서드를_매핑한다(final RequestMethod requestMethod) throws Exception {
        final var request = request("/multiple-methods", requestMethod);
        final var response = mock(HttpServletResponse.class);

        final var handler = (HandlerExecution) handlerMapping.getHandler(request);

        assertThat(handler).isNotNull();
        assertThat(handler.handle(request, response).getObject("handler"))
                .isEqualTo("multiple");
    }

    @Test
    @DisplayName("여러 HTTP 메서드 매핑에 포함되지 않은 HTTP 메서드는 매핑하지 않는다")
    void 매핑되지_않은_HTTP_메서드를_제외한다() {
        final var request = request("/multiple-methods", RequestMethod.PUT);

        final var handler = handlerMapping.getHandler(request);

        assertThat(handler).isNull();
    }

    @Test
    @DisplayName("동일한 URL과 HTTP 메서드가 중복으로 매핑되면 초기화에 실패한다")
    void 중복_매핑을_거부한다() {
        final var duplicateHandlerMapping = new AnnotationHandlerMapping(
                "com.interface21.webmvc.servlet.mvc.tobe"
        );

        assertThatThrownBy(duplicateHandlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class);
    }

    private HttpServletRequest request(final String requestUri, final RequestMethod requestMethod) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(requestUri);
        when(request.getMethod()).thenReturn(requestMethod.name());
        return request;
    }

    @Controller
    public static class DuplicateMappingController {

        @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
        public ModelAndView first(final HttpServletRequest request, final HttpServletResponse response) {
            return null;
        }

        @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
        public ModelAndView second(final HttpServletRequest request, final HttpServletResponse response) {
            return null;
        }
    }
}
