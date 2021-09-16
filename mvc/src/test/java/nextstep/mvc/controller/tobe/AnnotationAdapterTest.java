package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.RedirectView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationAdapterTest {

    private HandlerAdapter annotationAdapter;
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        annotationAdapter = new AnnotationAdapter();
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void modelAndView() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/integration-model-and-view");
        when(request.getMethod()).thenReturn("GET");

        final Object handlerExecution = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = annotationAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getView() instanceof JspView).isTrue();
    }

    @Test
    void string() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/integration-string");
        when(request.getMethod()).thenReturn("GET");

        final Object handlerExecution = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = annotationAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getView() instanceof JspView).isTrue();
    }

    @Test
    void stringRedirect() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/integration-string-redirect");
        when(request.getMethod()).thenReturn("GET");

        final Object handlerExecution = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = annotationAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getView() instanceof RedirectView).isTrue();
    }
}
