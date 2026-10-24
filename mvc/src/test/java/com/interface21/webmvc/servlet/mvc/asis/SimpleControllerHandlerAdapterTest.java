package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.HandlerAdapter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimpleControllerHandlerAdapterTest {

    private final HandlerAdapter adapter = new SimpleControllerHandlerAdapter();

    @Test
    void supportsOnlyLegacyControllers() {
        assertThat(adapter.supports(new ForwardController("/index.jsp"))).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void adaptsLegacyViewNameAndPassesRequestAndResponse() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getParameter("view")).thenReturn("/legacy.jsp");
        when(request.getRequestDispatcher("/legacy.jsp")).thenReturn(dispatcher);
        final Controller controller = (req, res) -> {
            res.setStatus(201);
            return req.getParameter("view");
        };

        final var modelAndView = adapter.handle(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(dispatcher).forward(request, response);
        verify(response).setStatus(201);
    }

    @Test
    void preservesLegacyRedirect() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var modelAndView = adapter.handle(request, response, new ForwardController("redirect:/login"));
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(response).sendRedirect("/login");
    }
}
