package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

public class AnnotationHandlerAdapterTest {

    @Test
    void support() {
        final var handlerAdapter = mock(AnnotationHandlerAdapter.class);
        final HandlerExecution handler = mock(HandlerExecution.class);
        when(handlerAdapter.supports(handler)).thenReturn(true);

        final boolean actual = handlerAdapter.supports(handler);

        verify(handlerAdapter, times(1)).supports(handler);
        assertThat(actual).isTrue();
    }

    @Test
    void support_notHandler() {
        final var handlerAdapter = mock(AnnotationHandlerAdapter.class);
        when(handlerAdapter.supports("not-handler")).thenReturn(false);

        final boolean actual = handlerAdapter.supports("not-handler");

        verify(handlerAdapter, times(1)).supports("not-handler");
        assertThat(actual).isFalse();
    }

    @Test
    void handle_verifyInvocations() throws Exception {
        final var handlerAdapter = mock(AnnotationHandlerAdapter.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handler = mock(HandlerExecution.class);

        handlerAdapter.handle(request, response, handler);

        verify(handlerAdapter, times(1)).handle(request, response, handler);
    }

    @Test
    void handle_verifyResult() throws Exception {
        final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        ModelAndView result = handlerAdapter.handle(request, response, handlerMapping.getHandler(request));

        assertThat(result.getObject("id")).isEqualTo("gugu");
    }
}
