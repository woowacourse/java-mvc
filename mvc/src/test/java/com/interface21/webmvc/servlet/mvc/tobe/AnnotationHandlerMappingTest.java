package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
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
        when(request.getContextPath()).thenReturn("");
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
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/post-test");
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
        when(request.getContextPath()).thenReturn("/app");
        when(request.getRequestURI()).thenReturn("/app/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void 클래스_레벨_경로와_메서드_레벨_경로를_합쳐_핸들러를_찾는다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/prefix/test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("controller")).isEqualTo("prefix");
    }

    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void HTTP_메서드를_지정하지_않으면_모든_메서드로_핸들러를_찾는다(final RequestMethod requestMethod) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/all-methods-test");
        when(request.getMethod()).thenReturn(requestMethod.name());

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("method")).isEqualTo(requestMethod.name());
    }

    @Test
    void 지원하지_않는_HTTP_메서드는_null을_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("CONNECT");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void 다시_초기화해도_기존_매핑과_충돌하지_않는다() throws Exception {
        handlerMapping.initialize();

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void 같은_URL과_메서드가_중복_매핑되면_초기화에_실패한다() {
        final var duplicateMapping = new AnnotationHandlerMapping(DuplicateController.class.getPackageName());

        assertThatThrownBy(duplicateMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복된 매핑");
    }

    @Controller
    static class DuplicateController {

        @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
        public ModelAndView first(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""));
        }

        @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
        public ModelAndView second(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""));
        }
    }
}
