package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples1");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getAttributeNames()).thenReturn(new Enumeration<String>() {
            int count = 0;
            @Override
            public boolean hasMoreElements() {
                return count++ < 1;
            }

            @Override
            public String nextElement() {
                return "id";
            }
        });

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        assertThat(((JspView) modelAndView.getView()).getViewName()).isEqualTo("/get-test");
    }

    @Test
    void post() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");
        when(request.getAttributeNames()).thenReturn(new Enumeration<String>() {
            int count = 0;
            @Override
            public boolean hasMoreElements() {
                return count++ < 1;
            }

            @Override
            public String nextElement() {
                return "id";
            }
        });

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        assertThat(((JspView) modelAndView.getView()).getViewName()).isEqualTo("/post-test");
    }
}
