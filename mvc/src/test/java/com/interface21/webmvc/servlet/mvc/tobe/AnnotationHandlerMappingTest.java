package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
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
    void HTTP_메서드를_지정하지_않으면_모두_지원한다() {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            final var request = mock(HttpServletRequest.class);

            when(request.getRequestURI()).thenReturn("/all-methods-test");
            when(request.getMethod()).thenReturn(requestMethod.name());

            final var handlerExecution = handlerMapping.getHandler(request);
            assertThat(handlerExecution).as("%s 요청의 핸들러", requestMethod).isNotNull();
        }
    }

    @Test
    void GET_전용_매핑은_POST_요청에_매칭되지_않는다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("POST");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 같은_URL도_HTTP_메서드에_따라_다른_메서드를_실행한다() throws Exception {
        final var getRequest = mock(HttpServletRequest.class);
        final var postRequest = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(getRequest.getRequestURI()).thenReturn("/same-url-test");
        when(getRequest.getMethod()).thenReturn("GET");
        when(postRequest.getRequestURI()).thenReturn("/same-url-test");
        when(postRequest.getMethod()).thenReturn("POST");

        final var getHandler = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(postRequest);
        assertThat(getHandler).isNotNull();
        assertThat(postHandler).isNotNull();

        assertThat(getHandler.handle(getRequest, response).getObject("handler")).isEqualTo("get");
        assertThat(postHandler.handle(postRequest, response).getObject("handler")).isEqualTo("post");
    }

    @Test
    void 존재하지_않는_URL은_핸들러가_없다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/missing-test");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 정의되지_않은_HTTP_메서드는_핸들러가_없다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("PROPFIND");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 지정한_HTTP_메서드만_지원한다() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/multiple-methods-test");

        when(request.getMethod()).thenReturn("GET");
        assertThat(handlerMapping.getHandler(request)).isNotNull();

        when(request.getMethod()).thenReturn("POST");
        assertThat(handlerMapping.getHandler(request)).isNotNull();

        when(request.getMethod()).thenReturn("PUT");
        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 같은_URL과_HTTP_메서드가_중복되면_예외가_발생한다() {
        final var duplicateHandlerMapping = new AnnotationHandlerMapping("duplicatemapping");

        assertThatThrownBy(() -> duplicateHandlerMapping.initialize())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복된 요청 매핑")
                .hasMessageContaining("/duplicate-test")
                .hasMessageContaining("GET");
    }
}
