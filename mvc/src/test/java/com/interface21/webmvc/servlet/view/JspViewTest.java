package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    @DisplayName("리다이렉트 viewName의 경우 redirect 시킨다.")
    void redirect() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var view = new JspView("redirect:/index.jsp");

        // when
        view.render(Map.of(), request, response);

        // then
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("forward viewName의 경우 forward 시킨다.")
    void forward() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        final var view = new JspView("/index.jsp");

        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        // when
        view.render(Map.of("user", "gugu"), request, response);

        // then
        verify(request).setAttribute("user", "gugu");
        verify(dispatcher).forward(request, response);
    }
}
