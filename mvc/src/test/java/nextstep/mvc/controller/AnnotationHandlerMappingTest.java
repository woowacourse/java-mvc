package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples1");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping
            .getHandler(request);
        final Object result = handlerExecution.handle(request, response);
        final ModelAndView modelAndView = (ModelAndView) result;

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        assertThat(((JspView) modelAndView.getView()).getViewName()).isEqualTo("/get-test");
    }

    @Test
    void post() throws Exception {
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping
            .getHandler(request);
        final Object result = handlerExecution.handle(request, response);
        final ModelAndView modelAndView = (ModelAndView) result;

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        assertThat(((JspView) modelAndView.getView()).getViewName()).isEqualTo("/post-test");
    }
}
