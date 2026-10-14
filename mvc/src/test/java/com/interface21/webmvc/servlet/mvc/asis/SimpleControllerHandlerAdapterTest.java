package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimpleControllerHandlerAdapterTest {

    private final SimpleControllerHandlerAdapter adapter = new SimpleControllerHandlerAdapter();

    @Test
    void supportsOnlyLegacyControllers() {
        assertThat(adapter.supports(mock(Controller.class))).isTrue();
        assertThat(adapter.supports(mock(HandlerExecution.class))).isFalse();
    }

    @Test
    void rendersLegacyRedirect() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final Controller controller = (req, res) -> "redirect:/login";

        final var modelAndView = adapter.handle(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(response).sendRedirect("/login");
    }

    @Test
    void forwardsUsingTheRequestPopulatedByLegacyController() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/legacy.jsp")).thenReturn(dispatcher);
        final Controller controller = (req, res) -> {
            req.setAttribute("id", "gugu");
            return "/legacy.jsp";
        };

        final var modelAndView = adapter.handle(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(request).setAttribute("id", "gugu");
        verify(dispatcher).forward(request, response);
    }
}
