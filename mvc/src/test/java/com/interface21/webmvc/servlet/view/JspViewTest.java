package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void givenRedirectView_whenRenders_thenRedirectsToSpecifiedPath() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var view = new JspView("redirect:/index.jsp");

        view.render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void givenJspView_whenRenders_thenForwardsToSpecifiedPath() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/index.jsp"))
                .thenReturn(requestDispatcher);

        final var view = new JspView("/index.jsp");

        view.render(Map.of(), request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void givenModel_whenRenders_thenAddsAttributesToRequest() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/index.jsp"))
                .thenReturn(requestDispatcher);

        final var view = new JspView("/index.jsp");

        view.render(Map.of("id", "gugu"), request, response);

        verify(request).setAttribute("id", "gugu");
    }
}
