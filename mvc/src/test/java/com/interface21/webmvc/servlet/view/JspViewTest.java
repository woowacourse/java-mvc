package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @DisplayName("viewName이 'redirect:'로 시작하지 않으면 포워딩한다")
    @Test
    void forward() throws Exception {
        final JspView jspView = new JspView("/get-test");
        final var request = mock(HttpServletRequest.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestDispatcher("/get-test")).thenReturn(requestDispatcher);

        jspView.render(new HashMap<>(), request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @DisplayName("viewName이 'redirect:'로 시작하면 리다이렉트한다")
    @Test
    void redirect() throws Exception {
        final JspView jspView = new JspView("redirect:/get-test");
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        jspView.render(new HashMap<>(), request, response);

        verify(response).sendRedirect("/get-test");
    }
}
