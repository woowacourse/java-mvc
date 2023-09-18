package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JspView;

class AnnotationHandlerAdapterTest {

    @Test
    void isSupport() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        HandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        Object handler = handlerMapping.getHandler(request);
        AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();

        assertThat(adapter.isSupport(handler)).isTrue();
    }

    @Test
    void handle() throws Exception {
        AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();
        HandlerExecution handler = mock(HandlerExecution.class);
        when(handler.handle(null, null)).thenReturn(new ModelAndView(new JspView("/index.jsp")));

        ModelAndView modelAndView = adapter.handle(handler, null, null);
        View result = modelAndView.getView();
        assertThat(result).isInstanceOf(JspView.class);
    }
}
