package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

public class AnnotationHandlerAdapterTest {

    @Test
    void support() {
        final var handlerAdapter = mock(AnnotationHandlerAdapter.class);
        final HandlerExecution handler = mock(HandlerExecution.class);
        when(handlerAdapter.supports(handler)).thenReturn(true);
        final boolean actual = handlerAdapter.supports(handler);

        assertThat(actual).isTrue();
    }

    @Test
    void support_notHandler() {
        final var handlerAdapter = mock(AnnotationHandlerAdapter.class);
        when(handlerAdapter.supports("not-handler")).thenReturn(false);
        final boolean actual = handlerAdapter.supports("not-handler");

        assertThat(actual).isFalse();
    }

    @Test
    void handle() throws Exception {
        final var handlerAdapter = mock(AnnotationHandlerAdapter.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handler = mock(HandlerExecution.class);

        when(handlerAdapter.handle(request, response, handler)).thenReturn(new ModelAndView(mock(JspView.class)));
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        verify(handlerAdapter, times(1)).handle(request, response, handler);
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
