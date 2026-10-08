package com.interface21.webmvc.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter handlerAdapter = new ControllerHandlerAdapter();

    @Test
    void givenController_whenChecksSupport_thenReturnsTrue() {
        final Controller controller = (request, response) -> "/index.jsp";

        final var result = handlerAdapter.supports(controller);

        assertThat(result).isTrue();
    }

    @Test
    void givenUnsupportedHandler_whenChecksSupport_thenReturnsFalse() {
        final var handler = new Object();

        final var result = handlerAdapter.supports(handler);

        assertThat(result).isFalse();
    }

    @Test
    void givenController_whenHandles_thenReturnsModelAndView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final Controller controller = (req, res) -> "/index.jsp";

        final var modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
        assertThat(modelAndView.getModel()).isEmpty();
    }
}
