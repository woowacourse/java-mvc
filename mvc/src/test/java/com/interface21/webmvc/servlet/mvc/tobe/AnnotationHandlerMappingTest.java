package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
    }

    @Test
    void get() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void 등록되지_않은_URL이면_null을_반환한다() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/not-registered");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 등록된_URL이지만_지원하지_않는_HTTP_메서드면_null을_반환한다() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("DELETE");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 존재하지_않는_HTTP_메서드면_null을_반환한다() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("FOO");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 핸들러_메서드의_반환_타입이_ModelAndView가_아니면_생성할_때_예외가_발생한다() {
        assertThatThrownBy(() -> new AnnotationHandlerMapping("invalidsamples.returntype"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 핸들러_메서드의_파라미터가_요청과_응답이_아니면_생성할_때_예외가_발생한다() {
        assertThatThrownBy(() -> new AnnotationHandlerMapping("invalidsamples.parameter"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 핸들러_메서드의_파라미터에_요청과_응답_외의_타입이_있으면_생성할_때_예외가_발생한다() {
        assertThatThrownBy(() -> new AnnotationHandlerMapping("invalidsamples.extraparameter"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 핸들러_메서드의_파라미터_순서가_요청_응답_순이_아니면_생성할_때_예외가_발생한다() {
        assertThatThrownBy(() -> new AnnotationHandlerMapping("invalidsamples.parameterorder"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 같은_URL과_HTTP_메서드로_두_번_등록하면_생성할_때_예외가_발생한다() {
        assertThatThrownBy(() -> new AnnotationHandlerMapping("invalidsamples.duplicate"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void HTTP_메서드를_명시한_매핑이_생략한_매핑보다_우선한다() throws Exception {
        final AnnotationHandlerMapping overlapHandlerMapping = new AnnotationHandlerMapping("overlapsamples");

        final ModelAndView modelAndView = handle(overlapHandlerMapping, "/users", "GET");

        assertThat(modelAndView.getObject("handler")).isEqualTo("getOnly");
    }

    @Test
    void HTTP_메서드를_명시한_매핑이_있어도_나머지_메서드는_생략한_매핑이_처리한다() throws Exception {
        final AnnotationHandlerMapping overlapHandlerMapping = new AnnotationHandlerMapping("overlapsamples");

        final ModelAndView modelAndView = handle(overlapHandlerMapping, "/users", "POST");

        assertThat(modelAndView.getObject("handler")).isEqualTo("anyMethod");
    }

    @Test
    void RequestMapping이_없는_메서드는_핸들러로_등록하지_않는다() {
        assertThatCode(() -> new AnnotationHandlerMapping("helpersamples"))
                .doesNotThrowAnyException();
    }

    private ModelAndView handle(final AnnotationHandlerMapping mapping, final String uri, final String method) throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);

        final HandlerExecution handlerExecution = (HandlerExecution) mapping.getHandler(request);
        return handlerExecution.handle(request, response);
    }
}
