package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new AnnotationHandlerAdapter();
    }

    @Test
    void support() {
        final boolean actual = handlerAdapter.supports(mock(HandlerExecution.class));

        assertThat(actual).isTrue();
    }

    @Test
    void support_notHandler() {
        final boolean actual = handlerAdapter.supports(mock(String.class));

        assertThat(actual).isFalse();
    }

    @Test
    void handle() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handler = mock(HandlerExecution.class);

        when(handlerAdapter.handle(request, response, handler)).thenReturn(new ModelAndView(mock(JspView.class)));
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
