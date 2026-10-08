package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JspViewTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Test
    void render_WhenViewNameStartRedirect_ThenSendRedirect() throws Exception {

        new JspView("redirect:/index.jsp").render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
        verifyNoInteractions(request);
    }

    @Test
    void render_WhenViewNameHasNoRedirect_ThenForward() throws Exception {

        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        new JspView("/index.jsp").render(Map.of(), request, response);

        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }
}
